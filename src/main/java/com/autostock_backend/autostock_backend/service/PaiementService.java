package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.Facture;
import com.autostock_backend.autostock_backend.domain.entity.Paiement;
import com.autostock_backend.autostock_backend.domain.enums.ModePaiement;
import com.autostock_backend.autostock_backend.domain.enums.StatutFacture;
import com.autostock_backend.autostock_backend.repository.FactureRepository;
import com.autostock_backend.autostock_backend.repository.PaiementRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final FactureRepository factureRepository;
    private final FactureService factureService;

  
    public Paiement encaisserPaiement(Long idFacture, Double montant, ModePaiement modePaiement) {

        Facture facture = factureRepository.findById(idFacture)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        // Vérifications
        if (facture.getStatutFacture() == StatutFacture.PAYEE) {
            throw new IllegalStateException("Facture déjà soldée");
        }
        if (facture.getStatutFacture() == StatutFacture.ANNULEE) {
            throw new IllegalStateException("Impossible d'encaisser un paiement sur une facture annulée");
        }
        if (montant <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }
        if (montant > facture.getResteAPayer()) {
            throw new IllegalArgumentException("Montant supérieur au reste à payer");
        }

        // Création du paiement
        Paiement paiement = new Paiement();
        paiement.setFacture(facture);
        paiement.setMontant(montant);
        paiement.setModePaiement(modePaiement);
        paiement.setDatePaiement(LocalDateTime.now());

        paiementRepository.save(paiement);

        // Mise à jour de la facture
        facture.setMontantPaye(facture.getMontantPaye() + montant);
        factureService.recalculerStatut(facture);

        return paiement;
    }

    /**
     * Récupère tous les paiements associés à une facture
     *
     * @param idFacture ID de la facture
     * @return Liste de paiements
     */
    public List<Paiement> getPaiementsByFacture(Long idFacture) {
        factureRepository.findById(idFacture)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));
        return paiementRepository.findByFactureIdFacture(idFacture);
    }

    /**
     * Sauvegarde un paiement directement (optionnel, pour usages internes)
     * Met à jour le montant payé et le statut de la facture
     *
     * @param paiement Paiement à sauvegarder
     * @return Paiement sauvegardé
     */
    public Paiement savePaiement(Paiement paiement) {
        Facture facture = paiement.getFacture();
        if (facture == null) {
            throw new IllegalArgumentException("Paiement doit être associé à une facture");
        }

        // Mise à jour du montant payé
        facture.setMontantPaye(facture.getMontantPaye() + paiement.getMontant());
        factureService.recalculerStatut(facture);

        return paiementRepository.save(paiement);
    }
}

