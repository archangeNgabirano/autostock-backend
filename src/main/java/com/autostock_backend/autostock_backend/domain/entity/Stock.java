package com.autostock_backend.autostock_backend.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
    name = "stocks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"idPiece", "idEntrepot"})
)
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPiece", nullable = false)
    private Piece piece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrepot", nullable = false)
    private Entrepot entrepot;

    private Double quantiteActuelle = 0.0;

    private Double stockMin;

    // 🔑 Prix réel du stock courant (dernier lot)
    private Double prixAchat;

    private Boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cree_par", updatable = false)
    private Utilisateur creePar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifie_par")
    private Utilisateur modifiePar;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}
