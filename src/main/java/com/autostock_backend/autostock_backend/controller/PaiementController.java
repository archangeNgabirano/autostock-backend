package com.autostock_backend.autostock_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.dto.PaiementRequest;
import com.autostock_backend.autostock_backend.domain.entity.Paiement;
import com.autostock_backend.autostock_backend.service.PaiementService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/paiements")
@AllArgsConstructor
public class PaiementController {


    private final PaiementService paiementService;

    @PostMapping
    public Paiement encaisserPaiement(@RequestBody PaiementRequest request, @RequestParam Long idFacture) {
        return paiementService.encaisserPaiement(
                idFacture,
                request.getMontant(),
                request.getModePaiement()
        );
    }

    @GetMapping("/facture/{idFacture}")
    public List<Paiement> getPaiementsByFacture(@PathVariable Long idFacture) {
        return paiementService.getPaiementsByFacture(idFacture);
    }
}

