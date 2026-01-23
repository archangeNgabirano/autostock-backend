package com.autostock_backend.autostock_backend.domain.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.autostock_backend.autostock_backend.domain.entity.Facture;
import com.autostock_backend.autostock_backend.domain.entity.Vente;

public class VenteMapper {

    public static VenteResponseDto toResponseDto(Vente vente) {
        if (vente == null) return null;

        VenteResponseDto dto = new VenteResponseDto();
        dto.setIdVente(vente.getIdVente());
        dto.setNumeroVente(vente.getNumeroVente());
        dto.setDateVente(vente.getDateVente());
        dto.setTotal(vente.getTotal());
        dto.setTypeVente(vente.getTypeVente());
        dto.setStatut(vente.getStatut());
        dto.setIdEntrepot(vente.getIdEntrepot());
        dto.setIdUtilisateur(vente.getIdUtilisateur());

        // Client info
        if (vente.getClient() != null) {
            ClientDto clientDto = new ClientDto();
            clientDto.setIdClient(vente.getClient().getIdClient());
            clientDto.setNom(vente.getClient().getNom());
            clientDto.setTelephone(vente.getClient().getContact());
            dto.setClient(clientDto);
        }

        // Lignes
        if (vente.getLignes() != null && !vente.getLignes().isEmpty()) {
            List<LigneVenteDto> lignes = vente.getLignes().stream().map(l -> {
                LigneVenteDto ligneDto = new LigneVenteDto();
                ligneDto.setIdLigne(l.getIdLigne());
                ligneDto.setIdPiece(l.getIdPiece());
                ligneDto.setQuantite(l.getQuantite());
                ligneDto.setPrixVente(l.getPrixVente());
                ligneDto.setTotalLigne(l.getTotalLigne());
                return ligneDto;
            }).collect(Collectors.toList());
            dto.setLignes(lignes);
        }

        // Facture + paiements
        if (vente.getFacture() != null) {
            Facture facture = vente.getFacture();
            FactureDto factureDto = new FactureDto();
            factureDto.setIdFacture(facture.getIdFacture());
            factureDto.setNumeroFacture(facture.getNumeroFacture());
            factureDto.setDateFacture(facture.getDateFacture());
            factureDto.setMontantTotal(facture.getMontantTotal());
            factureDto.setStatutFacture(facture.getStatutFacture());
            factureDto.setIdVente(vente.getIdVente());

            if (facture.getPaiements() != null && !facture.getPaiements().isEmpty()) {
                List<PaiementDto> paiements = facture.getPaiements().stream().map(p -> {
                    PaiementDto pDto = new PaiementDto();
                    pDto.setIdPaiement(p.getIdPaiement());
                    pDto.setMontant(p.getMontant());
                    pDto.setModePaiement(p.getModePaiement());
                    pDto.setDatePaiement(p.getDatePaiement());
                    return pDto;
                }).collect(Collectors.toList());
                factureDto.setPaiements(paiements);
            } else {
                factureDto.setPaiements(new ArrayList<>());
            }

            dto.setFacture(factureDto);
        }

        return dto;
    }
}
