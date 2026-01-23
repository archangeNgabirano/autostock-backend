package com.autostock_backend.autostock_backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "lignes_vente")
@Data
public class LigneVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLigne;

    @ManyToOne
    @JoinColumn(name = "idVente") // retirer insertable=false, updatable=false
    private Vente vente;

    @ManyToOne()
    @JoinColumn(name = "idPiece", insertable = false, updatable = false)
    private Piece piece;
    private Long idPiece;

    @Column(nullable = false)
    private Double quantite;

    @Column(nullable = false)
    private Double prixVente;

    private Double totalLigne;
}
