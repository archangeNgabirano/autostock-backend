package com.autostock_backend.autostock_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.LigneVente;

public interface LigneVenteRepository extends JpaRepository<LigneVente, Long> {
}

