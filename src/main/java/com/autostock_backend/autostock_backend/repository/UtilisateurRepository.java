package com.autostock_backend.autostock_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
Optional<Utilisateur> findByEmail(String email);
}
