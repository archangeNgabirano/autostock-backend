package com.autostock_backend.autostock_backend.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ligne_commande_fournisseur")
public class LigneCommandeFournisseur {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idLigneCommandeFournisseur;


private Long quantiteCommandee;
private BigDecimal prixUnitaire;
@ManyToOne
@JoinColumn(name = "idPiece",insertable = false,updatable = false)
private Piece piece;
private Long idPiece;

@ManyToOne
@JoinColumn(name = "idCommande",insertable = false,updatable =  false)
private CommandeFournisseur commandeFournisseur;
private Long idCommande;
}
