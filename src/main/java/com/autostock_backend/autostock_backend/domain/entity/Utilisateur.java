package com.autostock_backend.autostock_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "utilisateurs")
public class Utilisateur {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idUtilisateur;
private String nom;
private String prenom;
private String email;
private String telephone;
private String motDePasse;
private Boolean actif = true;
}
