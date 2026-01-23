package com.autostock_backend.autostock_backend.domain.dto;

import lombok.Data;

@Data
public class PieceCreateUpdateDto {

    private Long idPiece; // optionnel pour update
    private String nom;
    private String description;
    private Double prixAchat;
    private Double prixVente;
    private Boolean actif;
    private Double stockMin;

    private Long idCategorie;
    private Long idSousCategorie;
}

