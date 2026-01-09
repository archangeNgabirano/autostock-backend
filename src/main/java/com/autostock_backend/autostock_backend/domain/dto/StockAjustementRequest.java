package com.autostock_backend.autostock_backend.domain.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAjustementRequest {


    @NotNull
    private Long idStock;

    @NotNull
    private Double nouvelleQuantite;

    // optionnels
    private Double nouveauPrixAchat;
    private Double nouveauPrixVente;

    @NotNull
    private Long idUtilisateur;
}

