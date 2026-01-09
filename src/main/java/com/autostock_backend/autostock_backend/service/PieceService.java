package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.repository.PieceRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PieceService {

    private final PieceRepository pieceRepository;

    /* ================= CREATE ================= */
    public Piece create(Piece piece) {

        if (piece.getNom() == null || piece.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de la pièce est obligatoire");
        }

        boolean exists = pieceRepository
                .existsByNomIgnoreCaseAndIdCategorieAndIdSousCategorie(
                        piece.getNom().trim(),
                        piece.getIdCategorie(),
                        piece.getIdSousCategorie()
                );

        if (exists) {
            throw new IllegalArgumentException(
                    "Une pièce avec ce nom existe déjà dans cette catégorie et sous-catégorie"
            );
        }

        piece.setNom(piece.getNom().trim());
        piece.setActif(true);

        return pieceRepository.save(piece);
    }

    /* ================= UPDATE ================= */
    public Piece update(Long id, Piece pieceRequest) {

        Piece existing = getById(id);

        boolean exists = pieceRepository
                .existsByNomIgnoreCaseAndIdCategorieAndIdSousCategorie(
                        pieceRequest.getNom().trim(),
                        pieceRequest.getIdCategorie(),
                        pieceRequest.getIdSousCategorie()
                );

        boolean samePiece =
                existing.getNom().equalsIgnoreCase(pieceRequest.getNom())
                && existing.getIdCategorie().equals(pieceRequest.getIdCategorie())
                && existing.getIdSousCategorie().equals(pieceRequest.getIdSousCategorie());

        if (exists && !samePiece) {
            throw new IllegalArgumentException(
                    "Une autre pièce avec ce nom existe déjà dans cette catégorie et sous-catégorie"
            );
        }

        existing.setNom(pieceRequest.getNom().trim());
        existing.setDescription(pieceRequest.getDescription());
        existing.setPrixAchat(pieceRequest.getPrixAchat());
        existing.setPrixVente(pieceRequest.getPrixVente());
        existing.setIdCategorie(pieceRequest.getIdCategorie());
        existing.setIdSousCategorie(pieceRequest.getIdSousCategorie());
        existing.setActif(pieceRequest.getActif());

        return pieceRepository.save(existing);
    }

    /* ================= READ ================= */
    public Piece getById(Long id) {
        return pieceRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Pièce introuvable")
                );
    }

    public List<Piece> getAll() {
        return pieceRepository.findByActifTrue();
    }

    public List<Piece> getBySousCategorie(Long idSousCategorie) {
        return pieceRepository.findByIdSousCategorieAndActifTrue(idSousCategorie);
    }

    /* ================= DELETE (SOFT) ================= */
    public void delete(Long id) {
        Piece piece = getById(id);
        piece.setActif(false);
        pieceRepository.save(piece);
    }
}

