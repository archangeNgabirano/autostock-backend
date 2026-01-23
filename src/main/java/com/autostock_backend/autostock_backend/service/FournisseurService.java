package com.autostock_backend.autostock_backend.service;


import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.Fournisseur;
import com.autostock_backend.autostock_backend.repository.FournisseurRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    public FournisseurService(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    // Retourner tous les fournisseurs
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    // Retourner un fournisseur par ID
    public Optional<Fournisseur> getFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    // Créer ou mettre à jour un fournisseur
    public Fournisseur saveFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    // Supprimer un fournisseur
    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }

    
}

 