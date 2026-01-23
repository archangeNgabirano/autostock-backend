package com.autostock_backend.autostock_backend.domain.dto;

import com.autostock_backend.autostock_backend.domain.enums.ModePaiement;

import lombok.Data;

@Data
public class PaiementRequest {
    private Long idFacture;
    private Double montant;
    private ModePaiement modePaiement;
    
}

