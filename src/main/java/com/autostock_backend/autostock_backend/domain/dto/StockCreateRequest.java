package com.autostock_backend.autostock_backend.domain.dto;




import lombok.Data;

@Data
public class StockCreateRequest {

     private Long idPiece;
    private Long idEntrepot;

    private Double quantite;
    private Double stockMin;

    private Double prixAchat;      // obligatoire à l’entrée
    private Double margePourcent;  // ex : 20

    private Long idUtilisateur;
}


