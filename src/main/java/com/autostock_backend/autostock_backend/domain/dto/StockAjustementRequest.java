package com.autostock_backend.autostock_backend.domain.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;


import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Data
public class StockAjustementRequest {

    @NotNull(message = "Le stock à ajuster est obligatoire")
    private Long idStock;

    @NotNull(message = "La nouvelle quantité est obligatoire")
    @PositiveOrZero(message = "La quantité doit être supérieure ou égale à zéro")
    private Double nouvelleQuantite;

    // Optionnels
    @PositiveOrZero(message = "Le nouveau prix d'achat doit être >= 0")
    private Double nouveauPrixAchat;

    @PositiveOrZero(message = "Le nouveau prix de vente doit être >= 0")
    private Double nouveauPrixVente;

    @PositiveOrZero(message = "La marge doit être >= 0")
    private Double margePourcent;

    @NotNull(message = "L'utilisateur effectuant l'ajustement est obligatoire")
    private Long idUtilisateur;

    @Size(max = 500, message = "Le commentaire ne peut dépasser 500 caractères")
    private String commentaire;

    @Size(max = 255, message = "Le motif ne peut dépasser 255 caractères")
    private String motif;
}


