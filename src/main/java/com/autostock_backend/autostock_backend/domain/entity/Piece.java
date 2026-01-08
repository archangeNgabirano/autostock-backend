package com.autostock_backend.autostock_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pieces")
@Data
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPiece;

    private String nom;
    private String description;

    private Double prixAchat;
    private Double prixVente;

    private Boolean actif = true;

    // Obligatoire
    @ManyToOne
    @JoinColumn(name = "idCategorie", nullable = false,insertable = false, updatable = false)
    private SousCategorie Categorie;
    private Long idCategorie;
     @ManyToOne
    @JoinColumn(name = "idSousCategorie", nullable = false, insertable = false, updatable = false)
    private SousCategorie sousCategorie;
    private Long idSousCategorie;

    // Optionnelle
    @ManyToOne
    @JoinColumn(name = "idNumeroPiece",insertable = false, updatable = false)
    private NumeroPiece numeroPiece;
    private Long idNumeroPiece;

}
