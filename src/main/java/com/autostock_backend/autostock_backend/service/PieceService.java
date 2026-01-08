package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.NumeroPiece;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;
import com.autostock_backend.autostock_backend.repository.NumeroPieceRepository;
import com.autostock_backend.autostock_backend.repository.PieceRepository;
import com.autostock_backend.autostock_backend.repository.SousCategorieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PieceService {

    private final PieceRepository pieceRepository;
    private final SousCategorieRepository sousCategorieRepository;
    private final NumeroPieceRepository numeroPieceRepository;

    /* CREATE */
    public Piece create(Piece piece) {

        // Sous catégorie obligatoire
        SousCategorie sousCategorie = sousCategorieRepository
                .findById(piece.getIdSousCategorie())
                .orElseThrow(() -> new RuntimeException("Sous catégorie introuvable"));
        piece.setSousCategorie(sousCategorie);

        // Numero piece optionnel
        if (piece.getNumeroPiece() != null &&
            piece.getIdNumeroPiece() != null) {

            NumeroPiece numeroPiece = numeroPieceRepository
                    .findById(piece.getIdNumeroPiece())
                    .orElseThrow(() -> new RuntimeException("Numero pièce introuvable"));

            piece.setNumeroPiece(numeroPiece);
        } else {
            piece.setNumeroPiece(null);
        }

        return pieceRepository.save(piece);
    }

    /* READ */
    public List<Piece> getAll() {
        return pieceRepository.findAll();
    }

    public Piece getById(Long id) {
        return pieceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pièce introuvable"));
    }

    /* UPDATE */
    public Piece update(Long id, Piece pieceRequest) {

        Piece piece = getById(id);

        piece.setNom(pieceRequest.getNom());
        piece.setDescription(pieceRequest.getDescription());
        piece.setPrixAchat(pieceRequest.getPrixAchat());
        piece.setPrixVente(pieceRequest.getPrixVente());
        piece.setActif(pieceRequest.getActif());

        // Sous catégorie obligatoire
        SousCategorie sousCategorie = sousCategorieRepository
                .findById(pieceRequest.getIdSousCategorie())
                .orElseThrow(() -> new RuntimeException("Sous catégorie introuvable"));
        piece.setSousCategorie(sousCategorie);

        // Numero piece optionnel
        if (pieceRequest.getNumeroPiece() != null &&
            pieceRequest.getIdNumeroPiece() != null) {

            NumeroPiece numeroPiece = numeroPieceRepository
                    .findById(pieceRequest.getIdNumeroPiece())
                    .orElseThrow(() -> new RuntimeException("Numero pièce introuvable"));
            piece.setNumeroPiece(numeroPiece);
        } else {
            piece.setNumeroPiece(null);
        }

        return pieceRepository.save(piece);
    }

    /* DELETE (soft delete) */
    public void delete(Long id) {
        Piece piece = getById(id);
        piece.setActif(false);
        pieceRepository.save(piece);
    }
}
