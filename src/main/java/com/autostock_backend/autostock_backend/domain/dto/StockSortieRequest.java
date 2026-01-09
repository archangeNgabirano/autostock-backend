package com.autostock_backend.autostock_backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockSortieRequest {

    @NotNull
    private Long idStock;

    @NotNull
    @Positive
    private Double quantite;

    @NotNull
    private Long idUtilisateur;
}

