package com.autostock_backend.autostock_backend.domain.dto;

import lombok.Data;

@Data
// DTO pour la création
public class SousCategorieCreateDto {
    private String nom;
    private String description;
    private Boolean actif;
    private Long idCategorie; // <-- juste l'id de la catégorie
}

