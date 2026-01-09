package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.dto.StockAjustementRequest;
import com.autostock_backend.autostock_backend.domain.dto.StockCreateRequest;
import com.autostock_backend.autostock_backend.domain.dto.StockSortieRequest;
import com.autostock_backend.autostock_backend.domain.dto.StockTransfertRequest;
import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.Stock;
import com.autostock_backend.autostock_backend.domain.entity.StockAudit;
import com.autostock_backend.autostock_backend.domain.entity.Utilisateur;
import com.autostock_backend.autostock_backend.domain.enums.ActionStock;
import com.autostock_backend.autostock_backend.domain.enums.TypeMouvement;
import com.autostock_backend.autostock_backend.repository.EntrepotRepository;
import com.autostock_backend.autostock_backend.repository.PieceRepository;
import com.autostock_backend.autostock_backend.repository.StockAuditRepository;
import com.autostock_backend.autostock_backend.repository.StockRepository;
import com.autostock_backend.autostock_backend.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockRepository stockRepository;
    private final StockAuditRepository stockAuditRepository;
    private final PieceRepository pieceRepository;
    private final EntrepotRepository entrepotRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ================= APPROVISIONNEMENT =================
   @Transactional
public Stock enregistrerApprovisionnement(StockCreateRequest request) {

    Utilisateur utilisateur = getUserFromDTO(request.getIdUtilisateur());

    Piece piece = pieceRepository.findById(request.getIdPiece())
            .orElseThrow(() -> new RuntimeException("Pièce introuvable"));

    Entrepot entrepot = entrepotRepository.findById(request.getIdEntrepot())
            .orElseThrow(() -> new RuntimeException("Entrepôt introuvable"));

    Stock stock = stockRepository
            .findByPieceIdPieceAndEntrepotIdEntrepot(
                    piece.getIdPiece(),
                    entrepot.getIdEntrepot())
            .orElseGet(() -> createStock(piece, entrepot, request, utilisateur));

    // ================= AVANT =================
    double quantiteAvant = stock.getQuantiteActuelle();
    Double prixAchatStockAvant = stock.getPrixAchat();
    Double prixAchatPieceAvant = piece.getPrixAchat();
    Double prixVenteAvant = piece.getPrixVente();

    // ================= QUANTITÉ =================
    double quantiteApres = quantiteAvant + request.getQuantite();
    stock.setQuantiteActuelle(quantiteApres);
    stock.setStockMin(request.getStockMin());

    // ================= PRIX ACHAT LOT =================
    stock.setPrixAchat(request.getPrixAchat());

    // ================= PRIX ACHAT MOYEN PIECE =================
    double prixAchatMoyen;

    if (quantiteAvant == 0 || prixAchatPieceAvant == null) {
        prixAchatMoyen = request.getPrixAchat();
    } else {
        prixAchatMoyen =
                ((quantiteAvant * prixAchatPieceAvant)
                + (request.getQuantite() * request.getPrixAchat()))
                / quantiteApres;
    }

    piece.setPrixAchat(prixAchatMoyen);

    // ================= PRIX DE VENTE =================
    double marge = request.getMargePourcent() != null
            ? request.getMargePourcent() / 100
            : 0.20; // marge par défaut 20%

    double prixVente = prixAchatMoyen * (1 + marge);
    piece.setPrixVente(prixVente);

    // ================= META =================
    stock.setModifiePar(utilisateur);
    stock.setDateModification(LocalDateTime.now());

    pieceRepository.save(piece);
    Stock savedStock = stockRepository.saveAndFlush(stock);

    // ================= AUDIT =================
    StockAudit audit = new StockAudit();
    audit.setStock(savedStock);
    audit.setUtilisateur(utilisateur);
    audit.setAction(quantiteAvant == 0 ? ActionStock.CREATE : ActionStock.UPDATE);
    audit.setTypeMouvement(TypeMouvement.ENTREE);

    audit.setQuantiteAvant(quantiteAvant);
    audit.setQuantiteApres(savedStock.getQuantiteActuelle());

    audit.setPrixAchatAvant(prixAchatStockAvant);
    audit.setPrixAchatApres(savedStock.getPrixAchat());

    audit.setPrixVenteAvant(prixVenteAvant);
    audit.setPrixVenteApres(piece.getPrixVente());

    audit.setDateAction(LocalDateTime.now());

    stockAuditRepository.save(audit);

    return savedStock;
}


    // ================= AJUSTEMENT =================
  @Transactional
public Stock ajusterStock(StockAjustementRequest request) {

    Utilisateur utilisateur = getUserFromDTO(request.getIdUtilisateur());

    Stock stock = stockRepository.findById(request.getIdStock())
            .orElseThrow(() -> new RuntimeException("Stock introuvable"));

    Piece piece = stock.getPiece();

    // ================= AVANT =================
    double quantiteAvant = stock.getQuantiteActuelle();
    Double prixAchatAvant = stock.getPrixAchat();
    Double prixVenteAvant = piece.getPrixVente();

    // ================= QUANTITÉ =================
    stock.setQuantiteActuelle(request.getNouvelleQuantite());

    // ================= PRIX ACHAT =================
    if (request.getNouveauPrixAchat() != null) {
        stock.setPrixAchat(request.getNouveauPrixAchat());

        // mise à jour du prix par défaut de la pièce
        piece.setPrixAchat(request.getNouveauPrixAchat());
    }

    // ================= PRIX VENTE =================
    if (request.getNouveauPrixVente() != null) {
        piece.setPrixVente(request.getNouveauPrixVente());
    }

    // ================= META =================
    stock.setModifiePar(utilisateur);
    stock.setDateModification(LocalDateTime.now());

    pieceRepository.save(piece);
    Stock savedStock = stockRepository.saveAndFlush(stock);

    // ================= AUDIT =================
    StockAudit audit = new StockAudit();
    audit.setStock(savedStock);
    audit.setUtilisateur(utilisateur);
    audit.setAction(ActionStock.UPDATE);
    audit.setTypeMouvement(TypeMouvement.AJUSTEMENT);

    audit.setQuantiteAvant(quantiteAvant);
    audit.setQuantiteApres(savedStock.getQuantiteActuelle());

    audit.setPrixAchatAvant(prixAchatAvant);
    audit.setPrixAchatApres(savedStock.getPrixAchat());

    audit.setPrixVenteAvant(prixVenteAvant);
    audit.setPrixVenteApres(piece.getPrixVente());

    audit.setDateAction(LocalDateTime.now());

    stockAuditRepository.save(audit);

    return savedStock;
}


@Transactional
public void transfererStock(StockTransfertRequest request) {

    Utilisateur utilisateur = getUserFromDTO(request.getIdUtilisateur());

    Stock stockSource = stockRepository.findById(request.getIdStockSource())
            .orElseThrow(() -> new RuntimeException("Stock source introuvable"));

    if (stockSource.getQuantiteActuelle() < request.getQuantite()) {
        throw new RuntimeException("Quantité insuffisante pour le transfert");
    }

    Entrepot entrepotDestination = entrepotRepository
            .findById(request.getIdEntrepotDestination())
            .orElseThrow(() -> new RuntimeException("Entrepôt destination introuvable"));

    Stock stockDestination = stockRepository
            .findByPieceIdPieceAndEntrepotIdEntrepot(
                    stockSource.getPiece().getIdPiece(),
                    entrepotDestination.getIdEntrepot())
            .orElseGet(() -> createStock(
                    stockSource.getPiece(),
                    entrepotDestination,
                    request,
                    utilisateur));

    // ================= SORTIE =================
    double quantiteAvantSource = stockSource.getQuantiteActuelle();
    Double prixAchatSource = stockSource.getPrixAchat();

    stockSource.setQuantiteActuelle(quantiteAvantSource - request.getQuantite());
    stockSource.setModifiePar(utilisateur);
    stockSource.setDateModification(LocalDateTime.now());

    stockRepository.save(stockSource);

    saveAuditTransfert(
            stockSource,
            utilisateur,
            quantiteAvantSource,
            stockSource.getQuantiteActuelle(),
            prixAchatSource,
            prixAchatSource,
            "SORTIE"
    );

    // ================= ENTREE =================
    double quantiteAvantDest = stockDestination.getQuantiteActuelle();

    stockDestination.setQuantiteActuelle(quantiteAvantDest + request.getQuantite());

    // IMPORTANT : conserver le prix du lot
    stockDestination.setPrixAchat(prixAchatSource);

    stockDestination.setModifiePar(utilisateur);
    stockDestination.setDateModification(LocalDateTime.now());

    stockRepository.save(stockDestination);

    saveAuditTransfert(
            stockDestination,
            utilisateur,
            quantiteAvantDest,
            stockDestination.getQuantiteActuelle(),
            prixAchatSource,
            prixAchatSource,
            "ENTREE"
    );
}

private void saveAuditTransfert(
        Stock stock,
        Utilisateur utilisateur,
        double quantiteAvant,
        double quantiteApres,
        Double prixAchatAvant,
        Double prixAchatApres,
        String sens
) {
    StockAudit audit = new StockAudit();
    audit.setStock(stock);
    audit.setUtilisateur(utilisateur);
    audit.setAction(ActionStock.UPDATE);
    audit.setTypeMouvement(TypeMouvement.TRANSFERT);

    audit.setQuantiteAvant(quantiteAvant);
    audit.setQuantiteApres(quantiteApres);

    audit.setPrixAchatAvant(prixAchatAvant);
    audit.setPrixAchatApres(prixAchatApres);

    audit.setDateAction(LocalDateTime.now());

    stockAuditRepository.save(audit);
}


public Stock sortieStock(StockSortieRequest request) {

    Utilisateur utilisateur = getUserFromDTO(request.getIdUtilisateur());

    Stock stock = stockRepository.findById(request.getIdStock())
            .orElseThrow(() -> new RuntimeException("Stock introuvable"));

    if (stock.getQuantiteActuelle() < request.getQuantite()) {
        throw new RuntimeException("Stock insuffisant");
    }

    double avant = stock.getQuantiteActuelle();
    double apres = avant - request.getQuantite();

    stock.setQuantiteActuelle(apres);
    stock.setModifiePar(utilisateur);
    stock.setDateModification(LocalDateTime.now());

    Stock savedStock = stockRepository.saveAndFlush(stock);

    saveAudit(
            savedStock,
            utilisateur,
            ActionStock.UPDATE,
            avant,
            apres,
            TypeMouvement.SORTIE
    );

    return savedStock;
}


    // ================= DESACTIVATION =================
    public void desactiverStock(Long idStock, Long idUtilisateur) {

        Utilisateur utilisateur = getUserFromDTO(idUtilisateur);

        Stock stock = stockRepository.findById(idStock)
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));

        stock.setActif(false);
        stock.setModifiePar(utilisateur);
        stock.setDateModification(LocalDateTime.now());

        Stock savedStock = stockRepository.saveAndFlush(stock);

        saveAudit(
                savedStock,
                utilisateur,
                ActionStock.DELETE,
                savedStock.getQuantiteActuelle(),
                savedStock.getQuantiteActuelle(),
                TypeMouvement.AJUSTEMENT
        );
    }

    // ================= MÉTHODES PRIVÉES =================

    private Stock createStock(
            Piece piece,
            Entrepot entrepot,
            Object request,
            Utilisateur user) {

        Stock stock = new Stock();
        stock.setPiece(piece);
        stock.setEntrepot(entrepot);
        stock.setQuantiteActuelle(0.0);

        if (request instanceof StockCreateRequest scr) {
            stock.setStockMin(scr.getStockMin());
            stock.setPrixAchat(scr.getPrixAchat());
        }

        stock.setCreePar(user);
        stock.setDateCreation(LocalDateTime.now());
        stock.setActif(true);

        return stockRepository.saveAndFlush(stock);
    }

    private void saveAudit(
            Stock stock,
            Utilisateur user,
            ActionStock action,
            double avant,
            double apres,
            TypeMouvement mouvement) {

        StockAudit audit = new StockAudit();
        audit.setStock(stock);
        audit.setUtilisateur(user);
        audit.setAction(action);
        audit.setTypeMouvement(mouvement);
        audit.setQuantiteAvant(avant);
        audit.setQuantiteApres(apres);
        audit.setPrixAchatAvant(stock.getPrixAchat());
        audit.setPrixVenteAvant(stock.getPiece().getPrixVente());
        audit.setDateAction(LocalDateTime.now());

        stockAuditRepository.save(audit);
    }

    private Utilisateur getUserFromDTO(Long idUtilisateur) {
        if (idUtilisateur == null) {
            throw new RuntimeException("L'identifiant de l'utilisateur est requis");
        }
        return utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    // ================= READ =================
    public List<Stock> findAll() {
        return stockRepository.findByActifTrue();
    }

    public Stock findById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));
    }
}
