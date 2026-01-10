package com.autostock_backend.autostock_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.autostock_backend.autostock_backend.domain.entity.Piece;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Long> {

    boolean existsByNomIgnoreCaseAndIdCategorieAndIdSousCategorie(
            String nom,
            Long idCategorie,
            Long idSousCategorie);

    Optional<Piece> findByNomIgnoreCaseAndIdCategorieAndIdSousCategorie(
            String nom,
            Long idCategorie,
            Long idSousCategorie);

    List<Piece> findByIdSousCategorieAndActifTrue(Long idSousCategorie);

    // ================= CORRIGÉ =================
    @Query("SELECT p FROM Piece p " +
            "LEFT JOIN FETCH p.sousCategorie sc " +
            "LEFT JOIN FETCH sc.categorie " +
            "WHERE p.actif = true")
    List<Piece> findByActifTrueWithSousCategorie();

    @Query("SELECT p FROM Piece p LEFT JOIN FETCH p.sousCategorie sc LEFT JOIN FETCH sc.categorie")
    List<Piece> findAllWithSousCategorie();

    List<Piece> findByActifTrue();

    // <-- ajoute cette ligne pour récupérer uniquement les pièces inactives
    List<Piece> findByActifFalse();

}
