package com.autostock_backend.autostock_backend.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.dto.StockRequestDTO;
import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.domain.entity.NumeroPiece;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;
import com.autostock_backend.autostock_backend.domain.entity.Stock;
import com.autostock_backend.autostock_backend.domain.entity.StockAudit;
import com.autostock_backend.autostock_backend.domain.entity.Utilisateur;
import com.autostock_backend.autostock_backend.domain.enums.ActionStock;
import com.autostock_backend.autostock_backend.repository.EntrepotRepository;
import com.autostock_backend.autostock_backend.repository.NumeroPieceRepository;
import com.autostock_backend.autostock_backend.repository.PieceRepository;
import com.autostock_backend.autostock_backend.repository.SousCategorieRepository;
import com.autostock_backend.autostock_backend.repository.StockAuditRepository;
import com.autostock_backend.autostock_backend.repository.StockRepository;
import com.autostock_backend.autostock_backend.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Service
@Transactional
public class StockService {

    private final StockRepository stockRepository;
    private final StockAuditRepository stockAuditRepository;
    private final SousCategorieRepository sousCategorieRepository;
    private final NumeroPieceRepository numeroPieceRepository;
    private final PieceRepository pieceRepository;
    private final EntrepotRepository entrepotRepository;
    private final UtilisateurRepository utilisateurRepository;



    // ---------------- CREATE OR UPDATE STOCK ----------------
    public Stock createOrUpdateStock(StockRequestDTO dto) {

        Utilisateur user = getUserFromDTO(dto.getIdUtilisateur());

        // Récupération de la sous-catégorie
        SousCategorie sousCategorie = sousCategorieRepository.findById(dto.getIdSousCategorie())
                .orElseThrow(() -> new RuntimeException("Sous-catégorie introuvable"));

        // Gestion du numéro de pièce optionnel
        NumeroPiece numeroPiece = null;
        if (Boolean.TRUE.equals(sousCategorie.getAvecNumeroPiece())) {
            if (dto.getIdNumeroPiece() == null) {
                throw new RuntimeException("Numéro de pièce obligatoire pour cette sous-catégorie");
            }
            numeroPiece = numeroPieceRepository.findById(dto.getIdNumeroPiece())
                    .orElseThrow(() -> new RuntimeException("Numéro de pièce introuvable"));
        }

        // Trouver ou créer la pièce
        Piece piece = findOrCreatePiece(sousCategorie, numeroPiece);

        // Récupération de l'entrepôt
        Entrepot entrepot = entrepotRepository.findById(dto.getIdEntrepot())
                .orElseThrow(() -> new RuntimeException("Entrepôt introuvable"));

        // Trouver ou créer le stock
        Stock stock = stockRepository.findByPieceIdPieceAndEntrepotIdEntrepot(piece.getIdPiece(), entrepot.getIdEntrepot())
                .orElseGet(() -> {
                    Stock s = new Stock();
                    s.setPiece(piece);
                    s.setEntrepot(entrepot);
                    s.setQuantiteActuelle(0.0);
                    s.setCreePar(user);
                    s.setDateCreation(LocalDateTime.now());
                    s.setActif(true);
                    return stockRepository.save(s);
                });

        // Mise à jour de la quantité
        double avant = stock.getQuantiteActuelle();
        double apres = avant + dto.getQuantite();

        stock.setQuantiteActuelle(apres);
        stock.setModifiePar(user);
        stock.setDateModification(LocalDateTime.now());

        stockRepository.save(stock);

        // Audit
        audit(stock, user, ActionStock.CREATE, avant, apres);

        return stock;
    }

    // ---------------- PRIVATE METHODS ----------------
   private Piece findOrCreatePiece(SousCategorie sousCategorie, NumeroPiece numeroPiece) {
    if (numeroPiece != null) {
        // Une pièce avec numéro doit être unique
        return pieceRepository.findBySousCategorieAndNumeroPiece(sousCategorie, numeroPiece)
                .orElseGet(() -> createPiece(sousCategorie, numeroPiece));
    }

    // Si pas de numéro, prendre la première pièce existante sans numéro
    List<Piece> piecesSansNumero = pieceRepository.findBySousCategorieAndNumeroPieceIsNull(sousCategorie);
    if (!piecesSansNumero.isEmpty()) {
        return piecesSansNumero.get(0); // retourne la première existante
    }

    // Sinon créer une nouvelle pièce
    return createPiece(sousCategorie, null);
}


    private Piece createPiece(SousCategorie sousCategorie, NumeroPiece numeroPiece) {
        Piece p = new Piece();
        p.setSousCategorie(sousCategorie);
        p.setNumeroPiece(numeroPiece);
        p.setDescription(buildDesignation(sousCategorie, numeroPiece));
        p.setActif(true);
        return pieceRepository.save(p);
    }

    private String buildDesignation(SousCategorie sousCategorie, NumeroPiece numeroPiece) {
        StringBuilder designation = new StringBuilder();
        if (sousCategorie.getCategorie() != null) {
            designation.append(sousCategorie.getCategorie().getNom()).append(" - ");
        }
        designation.append(sousCategorie.getNom());
        if (numeroPiece != null && numeroPiece.getReference() != null) {
            designation.append(" (").append(numeroPiece.getReference()).append(")");
        }
        return designation.toString();
    }

    private void audit(Stock stock, Utilisateur user, ActionStock action, double avant, double apres) {
        StockAudit audit = new StockAudit();
        audit.setStock(stock);
        audit.setUtilisateur(user);
        audit.setAction(action);
        audit.setQuantiteAvant(avant);
        audit.setQuantiteApres(apres);
        audit.setDateAction(LocalDateTime.now());

        stockAuditRepository.save(audit);
    }

    private Utilisateur getUserFromDTO(Long idUtilisateur) {
        if (idUtilisateur == null) throw new RuntimeException("L'identifiant de l'utilisateur est requis");
        return utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    // ---------------- READ ----------------
    public List<Stock> findAll() {
        return stockRepository.findByActifTrue();
    }

    public Stock findById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));
    }
}
