package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.Facture;
import com.autostock_backend.autostock_backend.domain.entity.Vente;
import com.autostock_backend.autostock_backend.domain.enums.StatutFacture;
import com.autostock_backend.autostock_backend.repository.FactureRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;

    /** Crée une facture pour une vente */
    public Facture creerFacturePourVente(Vente vente) {

        Facture facture = new Facture();
        facture.setVente(vente);
        facture.setDateFacture(LocalDateTime.now());
        facture.setMontantTotal(vente.getTotal());
        facture.setMontantPaye(0.0);
        facture.setResteAPayer(vente.getTotal());
        facture.setNumeroFacture("FAC-" + System.currentTimeMillis()); // generate facture number
        facture.setStatutFacture(StatutFacture.IMPAYEE);

        // Si vente à crédit, associer le client
        // if (vente.getIdClient() != null) {
        // facture.set(vente.getIdClient());
        // }

        return factureRepository.save(facture);
    }

    /** Récupère la facture par ID de vente */
    public Facture getFactureByVente(Long idVente) {
        return factureRepository.findByVenteIdVente(idVente)
                .orElseThrow(() -> new RuntimeException("Facture introuvable pour la vente"));
    }

    /** Recalcule le statut de la facture selon le montant payé */
    public Facture recalculerStatut(Facture facture) {

        double reste = facture.getMontantTotal() - facture.getMontantPaye();
        facture.setResteAPayer(reste);

        if (reste <= 0) {
            facture.setStatutFacture(StatutFacture.PAYEE);
        } else if (facture.getMontantPaye() > 0) {
            facture.setStatutFacture(StatutFacture.PARTIELLE);
        } else {
            facture.setStatutFacture(StatutFacture.IMPAYEE);
        }

        return factureRepository.save(facture);
    }
}
