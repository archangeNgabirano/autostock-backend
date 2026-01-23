package com.autostock_backend.autostock_backend.domain.dto;

import java.util.List;

import com.autostock_backend.autostock_backend.domain.enums.TypeVente;

import lombok.Data;

@Data
public class VenteCreateRequest {

    private Long idClient;
    private Long idEntrepot;
    private Long idUtilisateur;
    private TypeVente typeVente;              // COMPTANT ou CREDIT
    private PaiementRequest paiement;         // optionnel si COMPTANT
    private List<LigneVenteRequest> lignes;
}

