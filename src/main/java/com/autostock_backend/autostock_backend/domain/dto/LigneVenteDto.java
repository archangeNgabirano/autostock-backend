package com.autostock_backend.autostock_backend.domain.dto;

import lombok.Data;

@Data
public class LigneVenteDto {
    private Long idLigne;
    private Long idPiece;
    private Double quantite;
    private Double prixVente;
    private Double totalLigne;
}