package com.autostock_backend.autostock_backend.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.autostock_backend.autostock_backend.domain.enums.StatutFacture;

import lombok.Data;

@Data
public class FactureDto {

    private Long idFacture;
    private String numeroFacture;
    private LocalDateTime dateFacture;
    private Double montantTotal;
    private StatutFacture statutFacture;
    

    private Long idVente; // référence plate vers la vente

    private List<PaiementDto> paiements;
}
