package com.autostock_backend.autostock_backend.domain.dto;

import lombok.Data;

@Data
public class StockTransfertRequest {
  Long idPiece;
  Long idEntrepotSource;
  Long idEntrepotDestination;
  Double quantite;
  Long idUtilisateur;
  String commentaire;

}



