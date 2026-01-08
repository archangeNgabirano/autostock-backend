package com.autostock_backend.autostock_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;

@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long> {

    List<SousCategorie> findByActifTrue();

    List<SousCategorie> findByIdCategorie(Long idCategorie);
}
