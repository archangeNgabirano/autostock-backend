package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.dto.LigneVenteRequest;
import com.autostock_backend.autostock_backend.domain.dto.VenteCreateRequest;
import com.autostock_backend.autostock_backend.domain.dto.VenteMapper;
import com.autostock_backend.autostock_backend.domain.dto.VenteResponseDto;
import com.autostock_backend.autostock_backend.domain.entity.ClientEntity;
import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.domain.entity.Facture;
import com.autostock_backend.autostock_backend.domain.entity.LigneVente;
import com.autostock_backend.autostock_backend.domain.entity.Paiement;
import com.autostock_backend.autostock_backend.domain.entity.Stock;
import com.autostock_backend.autostock_backend.domain.entity.StockAudit;
import com.autostock_backend.autostock_backend.domain.entity.Utilisateur;
import com.autostock_backend.autostock_backend.domain.entity.Vente;
import com.autostock_backend.autostock_backend.domain.enums.ActionStock;
import com.autostock_backend.autostock_backend.domain.enums.StatutFacture;
import com.autostock_backend.autostock_backend.domain.enums.StatutVente;
import com.autostock_backend.autostock_backend.domain.enums.TypeMouvement;
import com.autostock_backend.autostock_backend.domain.enums.TypeVente;
import com.autostock_backend.autostock_backend.repository.ClientRepository;
import com.autostock_backend.autostock_backend.repository.StockAuditRepository;
import com.autostock_backend.autostock_backend.repository.StockRepository;
import com.autostock_backend.autostock_backend.repository.UtilisateurRepository;
import com.autostock_backend.autostock_backend.repository.VenteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenteService {

    private final VenteRepository venteRepository;
    private final StockRepository stockRepository;
    private final StockAuditRepository stockAuditRepository;
    private final FactureService factureService;
    private final PaiementService paiementService;
    private final ClientRepository clientRepository;
    private final UtilisateurRepository utilisateurRepository;

    /* ===================== CREATION VENTE ===================== */
    @Transactional
    public VenteResponseDto creerVente(VenteCreateRequest request) {

        // Vérification client pour vente à crédit
        if (request.getTypeVente() == TypeVente.CREDIT && request.getIdClient() == null) {
            throw new RuntimeException("Pour une vente à crédit, l'ID du client est obligatoire");
        }
        // Load client entity
        ClientEntity client = clientRepository.findById(request.getIdClient())
        .orElseThrow(() -> new RuntimeException("Client introuvable"));

        // Set the entity on the vente

        // Création de la vente
        Vente vente = new Vente();
vente.setClient(client);
        vente.setIdEntrepot(request.getIdEntrepot());
        vente.setIdUtilisateur(request.getIdUtilisateur());
        vente.setDateVente(LocalDateTime.now());
        vente.setNumeroVente("VNT-" + System.currentTimeMillis());
        vente.setTypeVente(request.getTypeVente());
        vente.setStatut(StatutVente.VALIDEE);

        double total = 0;
        List<LigneVente> lignes = new ArrayList<>();

        // Traitement des lignes de vente et mise à jour stock
        for (LigneVenteRequest ligneReq : request.getLignes()) {
            Stock stock = stockRepository
                    .findByPieceIdPieceAndEntrepotIdEntrepot(ligneReq.getIdPiece(), request.getIdEntrepot())
                    .orElseThrow(() -> new RuntimeException(
                            "Stock introuvable pour la pièce ID " + ligneReq.getIdPiece()));

            if (stock.getQuantiteActuelle() < ligneReq.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour la pièce ID " + ligneReq.getIdPiece());
            }

            double avant = stock.getQuantiteActuelle();
            stock.setQuantiteActuelle(avant - ligneReq.getQuantite());
            stockRepository.save(stock);

            Utilisateur utilisateur = utilisateurRepository.findById(request.getIdUtilisateur())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            // Audit
            creerAudit(utilisateur, stock, ActionStock.DIMINUTION, TypeMouvement.VENTE,
                    avant, stock.getQuantiteActuelle(), vente.getEntrepot(), null, "Vente créée");

            LigneVente ligne = new LigneVente();
            ligne.setIdPiece(ligneReq.getIdPiece());
            ligne.setQuantite(ligneReq.getQuantite());
            ligne.setPrixVente(ligneReq.getPrixVente());
            ligne.setTotalLigne(ligneReq.getQuantite() * ligneReq.getPrixVente());
            ligne.setVente(vente);

            total += ligne.getTotalLigne();
            lignes.add(ligne);
        }

        vente.setLignes(lignes);
        vente.setTotal(total);

        // Sauvegarde vente
        Vente savedVente = venteRepository.save(vente);

        // Création facture
        // Création facture
        Facture facture = factureService.creerFacturePourVente(savedVente);
        savedVente.setFacture(facture); // link in memory

        // Paiement immédiat si COMPTANT
        if (request.getTypeVente() == TypeVente.COMPTANT && request.getPaiement() != null) {
            Paiement paiement = new Paiement();
            paiement.setFacture(facture);
            paiement.setMontant(request.getPaiement().getMontant());
            paiement.setModePaiement(request.getPaiement().getModePaiement());
            paiement.setDatePaiement(LocalDateTime.now());
            paiementService.savePaiement(paiement);

            // Add paiement to facture in memory
            facture.getPaiements().add(paiement);

            // Mise à jour facture
            factureService.recalculerStatut(facture);
        }

        // Now mapping will include facture, statut, and paiements
        return VenteMapper.toResponseDto(savedVente);

    }

    /* ===================== ANNULATION VENTE ===================== */
    @Transactional
    public Vente annulerVente(Long idVente, String motif) {

        Vente vente = venteRepository.findById(idVente)
                .orElseThrow(() -> new RuntimeException("Vente introuvable"));

        if (vente.getStatut() == StatutVente.ANNULEE) {
            throw new RuntimeException("Vente déjà annulée");
        }

        if (vente.getStatut() != StatutVente.VALIDEE) {
            throw new RuntimeException("Seule une vente VALIDEE peut être annulée");
        }

        // Retour en stock et audit
        for (LigneVente ligne : vente.getLignes()) {
            Stock stock = stockRepository
                    .findByPieceIdPieceAndEntrepotIdEntrepot(ligne.getIdPiece(), vente.getIdEntrepot())
                    .orElseThrow(() -> new RuntimeException(
                            "Stock introuvable pour la pièce ID : " + ligne.getIdPiece()));

            double avant = stock.getQuantiteActuelle();
            stock.setQuantiteActuelle(avant + ligne.getQuantite());
            stockRepository.save(stock);

            creerAudit(
                    vente.getUtilisateur(),
                    stock,
                    ActionStock.AUGMENTATION,
                    TypeMouvement.AJUSTEMENT,
                    avant,
                    stock.getQuantiteActuelle(),
                    vente.getEntrepot(),
                    null,
                    "Annulation vente : " + motif);
        }

        // Annulation de la facture
        Facture facture = factureService.getFactureByVente(vente.getIdVente());
        facture.setStatutFacture(StatutFacture.ANNULEE);
        factureService.recalculerStatut(facture);

        // Annulation des paiements
        if (facture.getPaiements() != null && !facture.getPaiements().isEmpty()) {
            facture.getPaiements().forEach(p -> p.setMontant(0.0));
        }

        // Annuler la vente
        vente.setStatut(StatutVente.ANNULEE);
        return venteRepository.save(vente);
    }

    /* ===================== CONSULTATION ===================== */
    public List<Vente> getAll() {
        return venteRepository.findAll();
    }

    public Vente getById(Long id) {
        return venteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente introuvable"));
    }

    public Vente getByNumero(String numero) {
        return venteRepository.findByNumeroVente(numero)
                .orElseThrow(() -> new RuntimeException("Vente introuvable"));
    }

    /* ===================== AUDIT HELPER ===================== */
    private void creerAudit(
            Utilisateur utilisateur,
            Stock stock,
            ActionStock action,
            TypeMouvement typeMouvement,
            Double avant,
            Double apres,
            Entrepot source,
            Entrepot destination,
            String commentaire) {

        StockAudit audit = new StockAudit();
        audit.setUtilisateur(utilisateur);
        audit.setStock(stock);
        audit.setAction(action);
        audit.setTypeMouvement(typeMouvement);
        audit.setQuantiteAvant(avant);
        audit.setQuantiteApres(apres);
        audit.setEntrepotSource(source);
        audit.setEntrepotDestination(destination);
        audit.setDateAction(LocalDateTime.now());
        audit.setCommentaire(commentaire);

        stockAuditRepository.save(audit);
    }
}
