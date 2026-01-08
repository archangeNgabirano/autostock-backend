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

@Data
@Entity
@Table(name = "numero_pieces")
public class NumeroPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNumeroPiece;
    private String reference; // exemple: para12, para16
    private Boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSousCategorie", insertable = false, updatable = false)
    private SousCategorie sousCategorie;
    private Long idSousCategorie; // clé étrangère vers SousCategorie

}
