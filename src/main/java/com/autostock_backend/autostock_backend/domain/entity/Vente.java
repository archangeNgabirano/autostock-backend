package com.autostock_backend.autostock_backend.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.autostock_backend.autostock_backend.domain.enums.StatutVente;
import com.autostock_backend.autostock_backend.domain.enums.TypeVente;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ventes")
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVente;

    @Column(nullable = false, unique = true)
    private String numeroVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idClient")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrepot", insertable = false, updatable = false)
    private Entrepot entrepot;
    private Long idEntrepot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur", insertable = false, updatable = false)
    private Utilisateur utilisateur;
    private Long idUtilisateur;

    @Column(nullable = false)
    private LocalDateTime dateVente;

    private Double total;

    @Enumerated(EnumType.STRING)
    private TypeVente typeVente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVente statut; // <-- ADD THIS BACK

    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL)
    private List<LigneVente> lignes;

    @OneToOne(mappedBy = "vente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Facture facture;

    
}

