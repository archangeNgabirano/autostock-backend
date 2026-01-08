package com.autostock_backend.autostock_backend.domain.entity;

import com.autostock_backend.autostock_backend.domain.enums.TypeEntrepot;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Data
@Table(name = "entrepots")
public class Entrepot {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idEntrepot;

private String nom;
private String localisation;
@Enumerated(EnumType.STRING)
private TypeEntrepot typeEntrepot; // CENTRAL / VENTE
private Boolean actif=true;
}
