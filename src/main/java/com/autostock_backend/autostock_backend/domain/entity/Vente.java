package com.autostock_backend.autostock_backend.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.autostock_backend.autostock_backend.domain.enums.StatutVente;

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
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ventes")
@Data
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVente;

    @Column(nullable = false, unique = true)
    private String numeroVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idClient", insertable = false, updatable = false)
    private ClientEntity client; // optionnel
    private Long idClient;

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
    private StatutVente statut;

    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL)
    private List<LigneVente> lignes;
}
