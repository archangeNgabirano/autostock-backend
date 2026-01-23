package com.autostock_backend.autostock_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    // Prix par défaut (optionnel)
    private Double prixAchat;
    private Double prixVente;
    private Double margePourcent;
    private Double stockMin;



    private Boolean actif = true;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategorie", nullable = false, insertable = false, updatable = false)
    private Categorie categorie;
    private Long idCategorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSousCategorie", nullable = false, insertable = false, updatable = false)
    private SousCategorie sousCategorie;
    private Long idSousCategorie;
}

