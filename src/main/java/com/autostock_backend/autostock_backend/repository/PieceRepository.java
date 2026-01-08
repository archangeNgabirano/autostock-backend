package com.autostock_backend.autostock_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autostock_backend.autostock_backend.domain.entity.NumeroPiece;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Long> {

    List<Piece> findByActifTrue();
   // Pièce avec sous-catégorie + numéro spécifique
      // Cherche une pièce unique avec sous-catégorie et numéro
    Optional<Piece> findBySousCategorieAndNumeroPiece(SousCategorie sousCategorie, NumeroPiece numeroPiece);

    // Cherche toutes les pièces sans numéro pour une sous-catégorie
    List<Piece> findBySousCategorieAndNumeroPieceIsNull(SousCategorie sousCategorie);
}

