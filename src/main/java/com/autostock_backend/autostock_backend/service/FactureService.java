package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.Facture;
import com.autostock_backend.autostock_backend.domain.entity.Paiement;
import com.autostock_backend.autostock_backend.domain.entity.Vente;
import com.autostock_backend.autostock_backend.domain.enums.StatutFacture;
import com.autostock_backend.autostock_backend.repository.FactureRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;

    /* ===================== CREATION FACTURE ===================== */
    @Transactional
    public Facture creerFacturePourVente(Vente vente) {

        if (vente == null) {
            throw new RuntimeException("Impossible de créer une facture : vente null");
        }

        // Vérifier si une facture existe déjà pour cette vente
        Facture existingFacture = factureRepository.findByVenteIdVente(vente.getIdVente()).orElse(null);
        if (existingFacture != null) {
            return existingFacture;
        }

        Facture facture = new Facture();
        facture.setVente(vente);

        // ===================== GENERATION NUMERO FACTURE =====================
        // Format pro : FACT-YYYYMMDD-XXXX (XXXX = timestamp ou séquence)
        String numero = "FACT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) 
                        + "-" + System.currentTimeMillis();
        facture.setNumeroFacture(numero);

        facture.setDateFacture(LocalDateTime.now());
        facture.setMontantTotal(vente.getTotal());
        facture.setMontantPaye(0.0);
        facture.setResteAPayer(vente.getTotal());
        facture.setStatutFacture(StatutFacture.NON_PAYEE);

        // Liste paiements vide initialement
        facture.setPaiements(new ArrayList<>());

        return factureRepository.save(facture);
    }

    /* ===================== RECHARGER STATUT ===================== */
    @Transactional
    public Facture recalculerStatut(Facture facture) {

        if (facture == null) {
            throw new RuntimeException("Facture null");
        }

        double montantPaye = facture.getPaiements().stream()
                .mapToDouble(Paiement::getMontant)
                .sum();

        facture.setMontantPaye(montantPaye);
        facture.setResteAPayer(facture.getMontantTotal() - montantPaye);

        // Mise à jour statut facture
        if (facture.getResteAPayer() <= 0) {
            facture.setStatutFacture(StatutFacture.PAYEE);
        } else if (montantPaye > 0) {
            facture.setStatutFacture(StatutFacture.PARTIELLE);
        } else {
            facture.setStatutFacture(StatutFacture.NON_PAYEE);
        }

        return factureRepository.save(facture);
    }

    /* ===================== RECUPERER FACTURE PAR VENTE ===================== */
    @Transactional
    public Facture getFactureByVente(Long idVente) {
        return factureRepository.findByVenteIdVente(idVente)
                .orElseThrow(() -> new RuntimeException("Facture introuvable pour la vente ID " + idVente));
    }
}
