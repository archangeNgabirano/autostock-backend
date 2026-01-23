package com.autostock_backend.autostock_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.CommandeFournisseur;
import com.autostock_backend.autostock_backend.domain.enums.StatutCommande;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur,Long> {
List<CommandeFournisseur> findByStatut(StatutCommande statut);
}
