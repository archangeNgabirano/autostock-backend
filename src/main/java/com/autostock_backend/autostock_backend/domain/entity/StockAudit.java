package com.autostock_backend.autostock_backend.domain.entity;

import java.time.LocalDateTime;

import com.autostock_backend.autostock_backend.domain.enums.ActionStock;
import com.autostock_backend.autostock_backend.domain.enums.TypeMouvement;

import jakarta.persistence.Column;
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
@Table(name = "stock_audit")
@Data
public class StockAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAudit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idStock", nullable = false)
    private Stock stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionStock action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement typeMouvement;

    private Double quantiteAvant;
    private Double quantiteApres;

    private Double prixAchatAvant;
    private Double prixAchatApres;

    private Double prixVenteAvant;
    private Double prixVenteApres;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrepotSource")
    private Entrepot entrepotSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrepotDestination")
    private Entrepot entrepotDestination;

    @Column(length = 255)
    private String motif;

    private Double margeAvant;
    private Double margeApres;

    @Column(length = 500)
    private String commentaire;

    @Column(nullable = false)
    private LocalDateTime dateAction;
}
