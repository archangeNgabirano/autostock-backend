package com.autostock_backend.autostock_backend.domain.dto;

import lombok.Data;

@Data
public class StockRequestDTO {

    private Long idPiece;
    private Long idEntrepot;
    private Double quantite;
    private Double stockMin;
    private Long idSousCategorie;

    // OPTIONNEL
    private Long idNumeroPiece;
    // Optional: only for frontend-sent user
    private Long idUtilisateur;
}
