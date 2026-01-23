package com.autostock_backend.autostock_backend.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.autostock_backend.autostock_backend.domain.entity.Facture;
import com.autostock_backend.autostock_backend.domain.entity.LigneVente;
import com.autostock_backend.autostock_backend.domain.entity.Paiement;
import com.autostock_backend.autostock_backend.domain.entity.Vente;

public class VenteMapper {

    public static VenteResponseDto toResponseDto(Vente vente) {

        if (vente == null) {
            return null;
        }

        VenteResponseDto dto = new VenteResponseDto();

        // =======================
        // INFORMATIONS DE BASE
        // =======================
        dto.setIdVente(vente.getIdVente());
        dto.setNumeroVente(vente.getNumeroVente());
        dto.setDateVente(vente.getDateVente());
        dto.setTotal(vente.getTotal());
        dto.setTypeVente(vente.getTypeVente());     // <-- FIXED
        dto.setStatut(vente.getStatut());

        dto.setIdClient(vente.getClient().getIdClient());
        dto.setIdEntrepot(vente.getIdEntrepot());
        dto.setIdUtilisateur(vente.getIdUtilisateur());

        // =======================
        // CLIENT (VENTE À CRÉDIT)
        // =======================
       if (vente.getClient() != null) {
    ClientDto clientDto = new ClientDto();
    clientDto.setIdClient(vente.getClient().getIdClient());
    clientDto.setNom(vente.getClient().getNom());
    clientDto.setTelephone(vente.getClient().getContact());
    dto.setClient(clientDto);
}

        // =======================
        // LIGNES DE VENTE
        // =======================
        List<LigneVenteDto> lignesDto = new ArrayList<>();

        if (vente.getLignes() != null) {
            for (LigneVente ligne : vente.getLignes()) {
                LigneVenteDto l = new LigneVenteDto();
                l.setIdLigne(ligne.getIdLigne());
                l.setIdPiece(ligne.getIdPiece());
                l.setQuantite(ligne.getQuantite());
                l.setPrixVente(ligne.getPrixVente());
                l.setTotalLigne(ligne.getTotalLigne());
                lignesDto.add(l);
            }
        }

        dto.setLignes(lignesDto); // <-- NEVER NULL

        // =======================
        // FACTURE
        // =======================
        if (vente.getFacture() != null) {

            Facture facture = vente.getFacture();
            FactureDto factureDto = new FactureDto();

            factureDto.setIdFacture(facture.getIdFacture());
            factureDto.setNumeroFacture(facture.getNumeroFacture()); // <-- FIXED
            factureDto.setDateFacture(facture.getDateFacture());
            factureDto.setMontantTotal(facture.getMontantTotal());
            factureDto.setStatutFacture(facture.getStatutFacture());
            factureDto.setIdVente(vente.getIdVente());

            // =======================
            // PAIEMENTS
            // =======================
            List<PaiementDto> paiementsDto = new ArrayList<>();

            if (facture.getPaiements() != null) {
                for (Paiement p : facture.getPaiements()) {
                    PaiementDto pDto = new PaiementDto();
                    pDto.setIdPaiement(p.getIdPaiement());
                    pDto.setMontant(p.getMontant());
                    pDto.setModePaiement(p.getModePaiement());
                    pDto.setDatePaiement(p.getDatePaiement());
                    paiementsDto.add(pDto);
                }
            }

            factureDto.setPaiements(paiementsDto); // <-- NEVER NULL
            dto.setFacture(factureDto);
        }

        return dto;
    }
}
