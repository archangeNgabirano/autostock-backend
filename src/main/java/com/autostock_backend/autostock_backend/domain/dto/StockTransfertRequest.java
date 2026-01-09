package com.autostock_backend.autostock_backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockTransfertRequest {

    @NotNull
    private Long idStockSource;

    @NotNull
    private Long idEntrepotDestination;

    @NotNull
    @Positive
    private Double quantite;

    @NotNull
    private Long idUtilisateur;

    /**
     * Utile pour créer le stock destination si inexistant
     */
    public StockCreateRequest toCreateRequest() {
        StockCreateRequest req = new StockCreateRequest();
        req.setIdEntrepot(idEntrepotDestination);
        req.setQuantite(0.0);
        req.setStockMin(0.0);
        req.setIdUtilisateur(idUtilisateur);
        return req;
    }
}

