package com.autostock_backend.autostock_backend.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.autostock_backend.autostock_backend.domain.enums.StatutVente;
import com.autostock_backend.autostock_backend.domain.enums.TypeVente;

import lombok.Data;

@Data
public class VenteResponseDto {

    private Long idVente;
    private String numeroVente;
    private LocalDateTime dateVente;
    private Double total;
    private TypeVente typeVente;
    private StatutVente statut;

    private Long idClient;
    private Long idEntrepot;
    private Long idUtilisateur;
    private ClientDto client;

    private List<LigneVenteDto> lignes; // Liste des lignes de vente

    private FactureDto facture; // ✅ Champ ajouté
}
