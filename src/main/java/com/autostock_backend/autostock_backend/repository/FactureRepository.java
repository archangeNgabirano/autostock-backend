package com.autostock_backend.autostock_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autostock_backend.autostock_backend.domain.entity.Facture;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    Optional<Facture> findByVenteIdVente(Long idVente);

}

