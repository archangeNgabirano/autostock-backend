package com.autostock_backend.autostock_backend.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="sous_categories")
public class SousCategorie {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idSousCategorie;
private String nom;
private String description;
private Boolean actif = true;

private Boolean avecNumeroPiece = false;
@ManyToOne
@JoinColumn(name = "idCategorie", nullable = false)
private Categorie categorie;

}