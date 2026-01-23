package com.autostock_backend.autostock_backend.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PieceDto {
    private Long idPiece;
    private String nom;
    private String description;
    private Double prixAchat;
    private Double prixVente;
    private Boolean actif;
    private Double stockMin;

    private Long idCategorie;
    private String nomCategorie;

    private Long idSousCategorie;
    private String nomSousCategorie;
}

