package com.autostock_backend.autostock_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Optional<Categorie> findByNom(String nom);
    List<Categorie> findByActifTrue();

    Optional<Categorie> findByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCase(String nom);

}
