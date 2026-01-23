package com.autostock_backend.autostock_backend.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.autostock_backend.autostock_backend.domain.enums.StatutFacture;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "factures")
@Data
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vente", nullable = false, unique = true)
    private Vente vente;

    @Column(nullable = false)
    private Double montantTotal;

    @Column(nullable = false)
    private Double montantPaye = 0.0;

    @Column(nullable = false)
    private Double resteAPayer;
    
    @Column(nullable = false, unique = true)
    private String numeroFacture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFacture statutFacture;

    @Column(nullable = false)
    private LocalDateTime dateFacture;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL)
    private List<Paiement> paiements = new ArrayList<>();
}
