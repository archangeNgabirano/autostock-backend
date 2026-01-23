package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.domain.entity.LigneVente;
import com.autostock_backend.autostock_backend.domain.entity.Stock;
import com.autostock_backend.autostock_backend.domain.entity.StockAudit;
import com.autostock_backend.autostock_backend.domain.entity.Utilisateur;
import com.autostock_backend.autostock_backend.domain.entity.Vente;
import com.autostock_backend.autostock_backend.domain.enums.ActionStock;
import com.autostock_backend.autostock_backend.domain.enums.StatutVente;
import com.autostock_backend.autostock_backend.domain.enums.TypeMouvement;
import com.autostock_backend.autostock_backend.repository.StockAuditRepository;
import com.autostock_backend.autostock_backend.repository.StockRepository;
import com.autostock_backend.autostock_backend.repository.VenteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenteService {

        private final VenteRepository venteRepository;
        private final StockRepository stockRepository;
        private final StockAuditRepository stockAuditRepository;

        /* ===================== CREATION ===================== */
      @Transactional
public Vente creerVente(Vente vente) {

    if (vente.getLignes() == null || vente.getLignes().isEmpty()) {
        throw new RuntimeException("Une vente doit contenir au moins une ligne");
    }

    vente.setNumeroVente("VNT-" + System.currentTimeMillis());
    vente.setDateVente(LocalDateTime.now());
    vente.setStatut(StatutVente.VALIDEE);
    vente.setTotal(0.0);

    double total = 0;
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setIdUtilisateur(vente.getIdUtilisateur());

    for (LigneVente ligne : vente.getLignes()) {

        // validations
        if (ligne.getQuantite() <= 0 || ligne.getPrixVente() < 0) {
            throw new RuntimeException("Quantité et prix doivent être positifs");
        }

        // vérifier stock
        Stock stock = stockRepository
                .findByPieceIdPieceAndEntrepotIdEntrepot(ligne.getIdPiece(), vente.getIdEntrepot())
                .orElseThrow(() -> new RuntimeException("Stock introuvable pour la pièce ID : " + ligne.getIdPiece()));

        if (stock.getQuantiteActuelle() < ligne.getQuantite()) {
            throw new RuntimeException("Stock insuffisant pour la pièce ID : " + ligne.getIdPiece());
        }

        // mettre à jour stock
        double avant = stock.getQuantiteActuelle();
        stock.setQuantiteActuelle(avant - ligne.getQuantite());
        stockRepository.save(stock);

        // attacher ligne à la vente
        ligne.setVente(vente);
        ligne.setTotalLigne(ligne.getQuantite() * ligne.getPrixVente());
        total += ligne.getTotalLigne();

        // audit
        creerAudit(
                utilisateur,
                stock,
                ActionStock.DIMINUTION,
                TypeMouvement.SORTIE,
                avant,
                stock.getQuantiteActuelle(),
                vente.getEntrepot(),
                null,
                "Vente " + vente.getNumeroVente()
        );
    }

    vente.setTotal(total);
    return venteRepository.save(vente); // cascade persiste les lignes
}

        /* ===================== ANNULATION ===================== */
        // @Transactional
        // public Vente annulerVente(Long idVente, String motif) {

        // Vente vente = venteRepository.findById(idVente)
        // .orElseThrow(() -> new RuntimeException("Vente introuvable"));

        // if (vente.getStatut() == StatutVente.ANNULEE) {
        // throw new RuntimeException("Vente déjà annulée");
        // }

        // if (vente.getStatut() != StatutVente.VALIDEE) {
        // throw new RuntimeException("Seule une vente VALIDEE peut être annulée");
        // }

        // for (LigneVente ligne : vente.getLignes()) {

        // Stock stock = stockRepository
        // .findByPieceIdPieceAndEntrepotIdEntrepot(
        // ligne.getPiece().getIdPiece(),
        // vente.getEntrepot().getIdEntrepot())
        // .orElseThrow(() -> new RuntimeException("Stock introuvable pour la pièce : "
        // + ligne.getPiece().getNom()));

        // double avant = stock.getQuantiteActuelle();

        // stock.setQuantiteActuelle(
        // avant + ligne.getQuantite());

        // stockRepository.save(stock);

        // creerAudit(
        // vente.getUtilisateur(),
        // stock,
        // ActionStock.AUGMENTATION,
        // TypeMouvement.AJUSTEMENT,
        // avant,
        // stock.getQuantiteActuelle(),
        // null,
        // vente.getEntrepot(),
        // "Annulation vente : " + motif);
        // }

        // vente.setStatut(StatutVente.ANNULEE);
        // return venteRepository.save(vente);
        // }

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

                if (utilisateur != null) {
                        audit.setUtilisateur(utilisateur);
                        audit.setIdUtilisateur(utilisateur.getIdUtilisateur());
                }

                if (stock != null) {
                        audit.setStock(stock);
                        audit.setIdStock(stock.getIdStock());
                }

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
