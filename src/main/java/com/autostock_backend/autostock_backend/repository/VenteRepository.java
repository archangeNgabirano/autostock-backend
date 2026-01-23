package com.autostock_backend.autostock_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.Vente;

public interface VenteRepository extends JpaRepository<Vente, Long> {
    Optional<Vente> findByNumeroVente(String numeroVente);
}

