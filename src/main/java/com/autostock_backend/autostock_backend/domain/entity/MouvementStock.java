package com.autostock_backend.autostock_backend.domain.entity;

import java.time.LocalDateTime;

import com.autostock_backend.autostock_backend.domain.enums.TypeMouvement;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mouvements_stock")
@Data
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMouvement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPiece", insertable = false, updatable = false)
    private Piece piece;
    private Long idPiece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrepotSource",insertable = false, updatable = false)
    private Entrepot entrepotSource;
    private Long idEntrepotSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrepotDestination",insertable = false, updatable = false)
    private Entrepot entrepotDestination;
    private Long idEntrepotDestination;

    @ManyToOne
    @JoinColumn(name = "idUtilisateur",insertable = false, updatable = false)
    private Entrepot Utilisateur;
    private Long idUtilisateur;

    @Enumerated(EnumType.STRING)
    private TypeMouvement typeMouvement;

    private Double quantite;

    private LocalDateTime dateMouvement;
}
