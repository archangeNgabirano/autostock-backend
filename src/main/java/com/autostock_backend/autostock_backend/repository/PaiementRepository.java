package com.autostock_backend.autostock_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autostock_backend.autostock_backend.domain.entity.Paiement;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByFactureIdFacture(Long idFacture);

}

