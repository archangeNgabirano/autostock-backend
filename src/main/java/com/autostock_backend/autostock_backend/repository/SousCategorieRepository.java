package com.autostock_backend.autostock_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autostock_backend.autostock_backend.domain.entity.Categorie;
import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;

@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long> {

    List<SousCategorie> findByActifTrue();

    // Check duplicate using the category object
    boolean existsByNomIgnoreCaseAndCategorie(String nom, Categorie categorie);

    // Check duplicate using the category ID
boolean existsByNomIgnoreCaseAndCategorie_IdCategorie(String nom, Long idCategorie);

    // Find all subcategories for a category
    List<SousCategorie> findByCategorie(Categorie categorie);

    // Find all subcategories by category ID
List<SousCategorie> findByCategorie_IdCategorie(Long idCategorie);
}
