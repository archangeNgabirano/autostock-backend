package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.BadRequestException;
import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.NotFoundException;
import com.autostock_backend.autostock_backend.domain.dto.StockAjustementRequest;
import com.autostock_backend.autostock_backend.domain.dto.StockCreateRequest;
import com.autostock_backend.autostock_backend.domain.dto.StockTransfertRequest;
import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.Stock;
import com.autostock_backend.autostock_backend.domain.entity.StockAudit;
import com.autostock_backend.autostock_backend.domain.entity.Utilisateur;
import com.autostock_backend.autostock_backend.domain.enums.ActionStock;
import com.autostock_backend.autostock_backend.domain.enums.StockStatus;
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

    private StockStatus calculerStatutStock(Stock stock) {

        if (stock.getQuantiteActuelle() == null || stock.getQuantiteActuelle() <= 0) {
            return StockStatus.OUT_OF_STOCK;
        }

        if (stock.getPiece().getStockMin() != null
                && stock.getQuantiteActuelle() <= stock.getPiece().getStockMin()) {
            return StockStatus.LOW_STOCK;
        }

        return StockStatus.IN_STOCK;
    }

    // ================= APPROVISIONNEMENT =================

    @Transactional
    public Stock enregistrerApprovisionnement(StockCreateRequest request) {

        Utilisateur utilisateur = getUserFromDTO(request.getIdUtilisateur());

        // ===================== Récupération des entités =====================
        Piece piece = pieceRepository.findById(request.getIdPiece())
                .orElseThrow(() -> new RuntimeException("Pièce introuvable"));

        Entrepot entrepot = entrepotRepository.findById(request.getIdEntrepot())
                .orElseThrow(() -> new RuntimeException("Entrepôt introuvable"));

        // ===================== Vérification stock existant =====================
        Optional<Stock> optionalStock = stockRepository
                .findByPieceIdPieceAndEntrepotIdEntrepot(piece.getIdPiece(), entrepot.getIdEntrepot());

        Stock stock;
        boolean isNewStock = false;

        if (optionalStock.isPresent()) {
            // Stock existant → UPDATE
            stock = optionalStock.get();
        } else {
            // Stock n'existe pas → CREATE
            stock = new Stock();
            stock.setPiece(piece);
            stock.setEntrepot(entrepot);
            stock.setQuantiteActuelle(0.0);
            stock.setPrixAchat(request.getPrixAchat());
            stock.setCreePar(utilisateur);
            stock.setDateCreation(LocalDateTime.now());
            stock.setStatus(calculerStatutStock(stock));
            stock = stockRepository.saveAndFlush(stock);
            isNewStock = true;
        }

        // ===================== Valeurs avant modification =====================
        double quantiteAvant = stock.getQuantiteActuelle();
        Double prixAchatStockAvant = stock.getPrixAchat();
        Double prixAchatPieceAvant = piece.getPrixAchat();
        Double prixVenteAvant = piece.getPrixVente();
        Double margeAvant = piece.getMargePourcent();

        // ===================== Quantité =====================
        double quantiteApres = quantiteAvant + request.getQuantite();
        stock.setQuantiteActuelle(quantiteApres);
        stock.setStatus(calculerStatutStock(stock));

        // ===================== Prix achat lot =====================
        stock.setPrixAchat(request.getPrixAchat());

        // ===================== Prix achat moyen de la pièce =====================
        double prixAchatMoyen;
        if (quantiteAvant == 0 || prixAchatPieceAvant == null) {
            prixAchatMoyen = request.getPrixAchat();
        } else {
            prixAchatMoyen = ((quantiteAvant * prixAchatPieceAvant)
                    + (request.getQuantite() * request.getPrixAchat()))
                    / quantiteApres;
        }
        piece.setPrixAchat(prixAchatMoyen);

        // ===================== MARGE =====================
        double marge = request.getMargePourcent() != null ? request.getMargePourcent() : 25.0;
        piece.setMargePourcent(marge);

        // ===================== Prix de vente =====================
        double prixVenteCalcule = prixAchatMoyen * (1 + marge / 100);
        piece.setPrixVente(prixVenteCalcule);

        // ===================== Méta =====================
        stock.setModifiePar(utilisateur);
        stock.setDateModification(LocalDateTime.now());

        // ===================== Sauvegarde =====================
        pieceRepository.save(piece);
        Stock savedStock = stockRepository.saveAndFlush(stock);

        // ===================== Audit =====================
        StockAudit audit = new StockAudit();
        audit.setStock(savedStock);
        audit.setUtilisateur(utilisateur);
        audit.setAction(isNewStock ? ActionStock.CREATE : ActionStock.UPDATE);
        audit.setTypeMouvement(TypeMouvement.ENTREE);

        audit.setQuantiteAvant(quantiteAvant);
        audit.setQuantiteApres(savedStock.getQuantiteActuelle());

        audit.setPrixAchatAvant(prixAchatStockAvant);
        audit.setPrixAchatApres(savedStock.getPrixAchat());

        audit.setPrixVenteAvant(prixVenteAvant);
        audit.setPrixVenteApres(piece.getPrixVente());

        audit.setMargeAvant(margeAvant);
        audit.setMargeApres(marge);

        audit.setDateAction(LocalDateTime.now());
        stockAuditRepository.save(audit);

        return savedStock;
    }

    // ================= AJUSTEMENT =================
    @Transactional
    public Stock ajusterStock(StockAjustementRequest request) {

        // ================= VALIDATIONS METIER =================
        if (request == null) {
            throw new IllegalArgumentException("La requête d’ajustement est obligatoire");
        }

        if (request.getIdStock() == null) {
            throw new IllegalArgumentException("Le stock à ajuster est obligatoire");
        }

        if (request.getNouvelleQuantite() == null || request.getNouvelleQuantite() < 0) {
            throw new IllegalArgumentException("La nouvelle quantité ne peut pas être négative");
        }

        if (request.getMotif() == null || request.getMotif().isBlank()) {
            throw new IllegalArgumentException("Le motif d’ajustement est obligatoire");
        }

        // ================= UTILISATEUR =================
        Utilisateur utilisateur = getUserFromDTO(request.getIdUtilisateur());

        // ================= STOCK =================
        Stock stock = stockRepository.findById(request.getIdStock())
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));

        Piece piece = stock.getPiece();

        // ================= SNAPSHOT AVANT =================
        double quantiteAvant = stock.getQuantiteActuelle();
        Double prixAchatAvant = stock.getPrixAchat();
        Double prixVenteAvant = piece.getPrixVente();
        Double margeAvant = piece.getMargePourcent();

        // ================= AJUSTEMENT QUANTITE =================
        double nouvelleQuantite = request.getNouvelleQuantite();
        stock.setQuantiteActuelle(nouvelleQuantite);
        stock.setStatus(calculerStatutStock(stock));

        // ================= AJUSTEMENT PRIX ACHAT =================
        if (request.getNouveauPrixAchat() != null && request.getNouveauPrixAchat() > 0) {
            stock.setPrixAchat(request.getNouveauPrixAchat());
            piece.setPrixAchat(request.getNouveauPrixAchat());
        }

        // ================= AJUSTEMENT MARGE =================
        Double marge = request.getMargePourcent() != null
                ? request.getMargePourcent()
                : margeAvant;

        piece.setMargePourcent(marge);

        // ================= RECALCUL PRIX VENTE =================
        if (piece.getPrixAchat() != null && marge != null) {
            double prixVenteCalcule = piece.getPrixAchat() * (1 + marge / 100);
            piece.setPrixVente(prixVenteCalcule);
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

        audit.setMargeAvant(margeAvant);
        audit.setMargeApres(marge);

        audit.setEntrepotSource(stock.getEntrepot());
        audit.setMotif(request.getMotif());
        audit.setCommentaire(request.getCommentaire());

        audit.setDateAction(LocalDateTime.now());

        stockAuditRepository.save(audit);

        return savedStock;
    }

   private void saveAuditTransfert(
        Stock stock,
        Utilisateur utilisateur,
        double quantiteAvant,
        double quantiteApres,
        Entrepot entrepotSource,
        Entrepot entrepotDestination,
        TypeMouvement typeMouvement,
        String commentaire) {

    if (stock == null || stock.getPiece() == null) {
        throw new IllegalStateException("Stock ou pièce null lors de l'audit de transfert");
    }

    Piece piece = stock.getPiece();

    StockAudit audit = new StockAudit();
    audit.setStock(stock);
    audit.setUtilisateur(utilisateur);

    // 🔑 SÉMANTIQUE CLAIRE
    audit.setAction(ActionStock.TRANSFERT);
    audit.setTypeMouvement(typeMouvement); // SORTIE ou ENTREE

    // ================= QUANTITÉ =================
    audit.setQuantiteAvant(quantiteAvant);
    audit.setQuantiteApres(quantiteApres);

    // ================= PRIX ACHAT =================
    // Pour un transfert → le prix ne change PAS
    audit.setPrixAchatAvant(stock.getPrixAchat());
    audit.setPrixAchatApres(stock.getPrixAchat());

    // ================= PRIX VENTE =================
    audit.setPrixVenteAvant(piece.getPrixVente());
    audit.setPrixVenteApres(piece.getPrixVente());

    // ================= MARGE =================
    audit.setMargeAvant(piece.getMargePourcent());
    audit.setMargeApres(piece.getMargePourcent());

    // ================= ENTREPOTS =================
    audit.setEntrepotSource(entrepotSource);
    audit.setEntrepotDestination(entrepotDestination);

    // ================= MÉTA =================
    audit.setMotif("TRANSFERT DE STOCK");
    audit.setCommentaire(
            commentaire != null && !commentaire.isBlank()
                    ? commentaire
                    : (typeMouvement == TypeMouvement.SORTIE
                            ? "Sortie de stock (transfert)"
                            : "Entrée de stock (transfert)")
    );

    audit.setDateAction(LocalDateTime.now());

    // IMPORTANT : flush pour éviter perte silencieuse
    stockAuditRepository.saveAndFlush(audit);
}

    @Transactional
public void transfererStock(StockTransfertRequest request) {

    // ================= VALIDATIONS =================
    if (request.getIdPiece() == null) {
        throw new BadRequestException("La pièce est obligatoire pour le transfert");
    }

    if (request.getQuantite() == null || request.getQuantite() <= 0) {
        throw new BadRequestException("La quantité transférée doit être strictement positive");
    }

    if (request.getIdEntrepotSource() == null || request.getIdEntrepotDestination() == null) {
        throw new BadRequestException("Les entrepôts source et destination sont obligatoires");
    }

    if (request.getIdEntrepotSource().equals(request.getIdEntrepotDestination())) {
        throw new BadRequestException("L'entrepôt source et destination doivent être différents");
    }

    if (request.getIdUtilisateur() == null) {
        throw new BadRequestException("Utilisateur obligatoire pour le transfert");
    }

    double quantite = request.getQuantite();
    Utilisateur utilisateur = getUserFromDTO(request.getIdUtilisateur());

    // ================= ENTREPOT SOURCE =================
    Entrepot entrepotSource = entrepotRepository.findById(request.getIdEntrepotSource())
            .orElseThrow(() -> new NotFoundException("Entrepôt source introuvable"));

    // ================= ENTREPOT DESTINATION =================
    Entrepot entrepotDestination = entrepotRepository.findById(request.getIdEntrepotDestination())
            .orElseThrow(() -> new NotFoundException("Entrepôt destination introuvable"));

    // ================= STOCK SOURCE =================
    Stock stockSource = stockRepository
            .findByPieceIdPieceAndEntrepotIdEntrepot(
                    request.getIdPiece(),
                    request.getIdEntrepotSource())
            .orElseThrow(() -> new NotFoundException("Stock source introuvable pour cette pièce"));

    if (stockSource.getQuantiteActuelle() < quantite) {
        throw new BadRequestException(
                "Stock insuffisant. Disponible : " + stockSource.getQuantiteActuelle());
    }

    // ================= STOCK DESTINATION =================
    Stock stockDestination = stockRepository
            .findByPieceIdPieceAndEntrepotIdEntrepot(
                    request.getIdPiece(),
                    request.getIdEntrepotDestination())
            .orElse(null);

    if (stockDestination == null) {
        stockDestination = new Stock();
        stockDestination.setPiece(stockSource.getPiece());
        stockDestination.setEntrepot(entrepotDestination);
        stockDestination.setQuantiteActuelle(0.0);
        stockDestination.setPrixAchat(stockSource.getPrixAchat());
        stockDestination.setStatus(StockStatus.OUT_OF_STOCK);
        stockDestination.setCreePar(utilisateur);
        stockDestination.setDateCreation(LocalDateTime.now());

        // 🔥 IMPORTANT
        stockDestination = stockRepository.saveAndFlush(stockDestination);
    }

    // ================= SORTIE (SOURCE) =================
    double qteAvantSource = stockSource.getQuantiteActuelle();
    double qteApresSource = qteAvantSource - quantite;

    stockSource.setQuantiteActuelle(qteApresSource);
    stockSource.setStatus(calculerStatutStock(stockSource));
    stockSource.setModifiePar(utilisateur);
    stockSource.setDateModification(LocalDateTime.now());

    stockSource = stockRepository.saveAndFlush(stockSource);

    saveAuditTransfert(
            stockSource,
            utilisateur,
            qteAvantSource,
            qteApresSource,
            entrepotSource,
            entrepotDestination,
            TypeMouvement.SORTIE,
            request.getCommentaire()
    );

    // ================= ENTREE (DESTINATION) =================
    double qteAvantDest = stockDestination.getQuantiteActuelle();
    double qteApresDest = qteAvantDest + quantite;

    stockDestination.setQuantiteActuelle(qteApresDest);
    stockDestination.setPrixAchat(stockSource.getPrixAchat());
    stockDestination.setStatus(calculerStatutStock(stockDestination));
    stockDestination.setModifiePar(utilisateur);
    stockDestination.setDateModification(LocalDateTime.now());

    stockDestination = stockRepository.saveAndFlush(stockDestination);

    saveAuditTransfert(
            stockDestination,
            utilisateur,
            qteAvantDest,
            qteApresDest,
            entrepotSource,
            entrepotDestination,
            TypeMouvement.ENTREE,
            request.getCommentaire()
    );
}


    // ================= DESACTIVATION =================
    public void desactiverStock(Long idStock, Long idUtilisateur) {

        Utilisateur utilisateur = getUserFromDTO(idUtilisateur);

        Stock stock = stockRepository.findById(idStock)
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));

        stock.setStatus(calculerStatutStock(stock));
        stock.setModifiePar(utilisateur);
        stock.setDateModification(LocalDateTime.now());

        Stock savedStock = stockRepository.saveAndFlush(stock);

        saveAudit(
                savedStock,
                utilisateur,
                ActionStock.DELETE,
                savedStock.getQuantiteActuelle(),
                savedStock.getQuantiteActuelle(),
                TypeMouvement.AJUSTEMENT);
    }

    // ================= MÉTHODES PRIVÉES =================

    // private Stock createStock(
    // Piece piece,
    // Entrepot entrepot,
    // Object request,
    // Utilisateur user) {

    // Stock stock = new Stock();
    // stock.setPiece(piece);
    // stock.setEntrepot(entrepot);
    // stock.setQuantiteActuelle(0.0);

    // if (request instanceof StockCreateRequest scr) {
    // stock.setPrixAchat(scr.getPrixAchat());
    // }

    // stock.setCreePar(user);
    // stock.setDateCreation(LocalDateTime.now());
    // stock.setStatus(calculerStatutStock(stock));
    // return stockRepository.saveAndFlush(stock);
    // }

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
        return stockRepository.findAll();
    }

    public Stock findById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));
    }
}
