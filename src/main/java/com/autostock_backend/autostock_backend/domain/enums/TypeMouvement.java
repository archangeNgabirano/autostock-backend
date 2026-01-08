package com.autostock_backend.autostock_backend.domain.enums;

public enum TypeMouvement {
    ENTREE,      // Approvisionnement / entrée en stock
    SORTIE,      // Vente / sortie vers client
    TRANSFERT,   // Déplacement entre entrepôts
    AJUSTEMENT   // Correction d’inventaire
}

