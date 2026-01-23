package com.autostock_backend.autostock_backend.domain.dto;

import java.time.LocalDateTime;

import com.autostock_backend.autostock_backend.domain.enums.ModePaiement;

public class PaiementDto {

    private Long idPaiement;
    private Double montant;
    private ModePaiement modePaiement;
    private LocalDateTime datePaiement;

    // Getters & Setters
    public Long getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(Long idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }
}

