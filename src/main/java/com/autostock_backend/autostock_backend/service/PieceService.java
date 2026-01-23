package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.BadRequestException;
import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.NotFoundException;
import com.autostock_backend.autostock_backend.domain.dto.PieceCreateUpdateDto;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;
import com.autostock_backend.autostock_backend.repository.PieceRepository;
import com.autostock_backend.autostock_backend.repository.SousCategorieRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PieceService {

    private final PieceRepository pieceRepository;
    private final SousCategorieRepository sousCategorieRepository;

  
    /* ================= CREATE ================= */
    public Piece create(PieceCreateUpdateDto dto) {
        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new BadRequestException("Le nom de la pièce est obligatoire");
        }

        // Vérifier doublon
        boolean exists = pieceRepository
                .existsByNomIgnoreCaseAndIdCategorieAndIdSousCategorie(
                        dto.getNom().trim(),
                        dto.getIdCategorie(),
                        dto.getIdSousCategorie()
                );

        if (exists) {
            throw new BadRequestException(
                    "Une pièce avec ce nom existe déjà dans cette catégorie et sous-catégorie"
            );
        }

        // Vérifier que la sous-catégorie existe
        SousCategorie sousCategorie = sousCategorieRepository
                .findById(dto.getIdSousCategorie())
                .orElseThrow(() -> new NotFoundException("Sous-catégorie introuvable"));

        // Créer la pièce
        Piece piece = new Piece();
        piece.setNom(dto.getNom().trim());
        piece.setDescription(dto.getDescription());
        piece.setPrixAchat(dto.getPrixAchat());
        piece.setPrixVente(dto.getPrixVente());
        piece.setActif(dto.getActif() != null ? dto.getActif() : true);
        piece.setStockMin(dto.getStockMin());

        piece.setSousCategorie(sousCategorie);
        piece.setIdSousCategorie(sousCategorie.getIdSousCategorie());
        piece.setCategorie(sousCategorie.getCategorie());
        piece.setIdCategorie(sousCategorie.getCategorie().getIdCategorie());

        return pieceRepository.save(piece);
    }

    /* ================= UPDATE ================= */
    public Piece update(Long id, PieceCreateUpdateDto dto) {
        Piece existing = getById(id);

        // Vérifier doublon
        boolean exists = pieceRepository
                .existsByNomIgnoreCaseAndIdCategorieAndIdSousCategorie(
                        dto.getNom().trim(),
                        dto.getIdCategorie(),
                        dto.getIdSousCategorie()
                );

        boolean samePiece = existing.getNom().equalsIgnoreCase(dto.getNom())
                && existing.getIdCategorie().equals(dto.getIdCategorie())
                && existing.getIdSousCategorie().equals(dto.getIdSousCategorie());

        if (exists && !samePiece) {
            throw new BadRequestException(
                    "Une autre pièce avec ce nom existe déjà dans cette catégorie et sous-catégorie"
            );
        }

        // Vérifier que la sous-catégorie existe
        SousCategorie sousCategorie = sousCategorieRepository
                .findById(dto.getIdSousCategorie())
                .orElseThrow(() -> new NotFoundException("Sous-catégorie introuvable"));

        // Mettre à jour
        existing.setNom(dto.getNom().trim());
        existing.setDescription(dto.getDescription());
        existing.setPrixAchat(dto.getPrixAchat());
        existing.setPrixVente(dto.getPrixVente());
        existing.setActif(dto.getActif() != null ? dto.getActif() : true);
        existing.setStockMin(dto.getStockMin());

        existing.setSousCategorie(sousCategorie);
        existing.setIdSousCategorie(sousCategorie.getIdSousCategorie());
        existing.setCategorie(sousCategorie.getCategorie());
        existing.setIdCategorie(sousCategorie.getCategorie().getIdCategorie());

        return pieceRepository.save(existing);
    }

    /* ================= READ ================= */
    public Piece getById(Long id) {
        return pieceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pièce introuvable"));
    }

    public List<Piece> getAll() {
        return pieceRepository.findByActifTrue(); // récupérer seulement les actives
    }

    public List<Piece> getInactive() {
        return pieceRepository.findByActifFalse(); // récupérer les inactives
    }

    /* ================= DELETE (SOFT) ================= */
    public void delete(Long id) {
        Piece existing = getById(id);
        existing.setActif(false);
        pieceRepository.save(existing);
    }
}
