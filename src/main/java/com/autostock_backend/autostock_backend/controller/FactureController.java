package com.autostock_backend.autostock_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.entity.Facture;
import com.autostock_backend.autostock_backend.service.FactureService;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @GetMapping("/vente/{idVente}")
    public Facture getFactureByVente(@PathVariable Long idVente) {
        return factureService.getFactureByVente(idVente);
    }
}

