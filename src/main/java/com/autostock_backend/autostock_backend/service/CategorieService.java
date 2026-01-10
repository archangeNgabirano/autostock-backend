package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.BadRequestException;
import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.NotFoundException;
import com.autostock_backend.autostock_backend.domain.entity.Categorie;
import com.autostock_backend.autostock_backend.repository.CategorieRepository;

import jakarta.transaction.Transactional;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    // Créer une seule catégorie
    public Categorie creerCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    

    // Créer plusieurs catégories
    // public List<Categorie> creerCategories(List<Categorie> categories) {
    //     return categorieRepository.saveAll(categories);
    // }

    // Lister toutes les catégories
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    // Récupérer une catégorie par ID
    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));
    }

    // Mettre à jour une catégorie
    // public Categorie updateCategorie(Long id, Categorie categorieDetails) {
    //     Categorie categorie = getCategorieById(id);
    //     categorie.setNom(categorieDetails.getNom());
    //     categorie.setDescription(categorieDetails.getDescription());
    //     categorie.setActif(categorieDetails.getActif() );
    //     return categorieRepository.save(categorie);
    // }


    
    // Créer plusieurs catégories
public Categorie createCategorie(Categorie categorie) {

        //Validation
        if (categorie.getNom() == null || categorie.getNom().trim().isEmpty()) {
            throw new BadRequestException("Le nom de la catégorie est obligatoire");
        }

        String nomNormalise = categorie.getNom().trim();

        //Duplicate check (case-insensitive)
        categorieRepository.findByNomIgnoreCase(nomNormalise)
                .ifPresent(c -> {
                    throw new BadRequestException(
                        "Une catégorie avec le nom '" + nomNormalise + "' existe déjà"
                    );
                });

        //Normalize + defaults
        categorie.setNom(nomNormalise);
        categorie.setActif(categorie.getActif() != null ? categorie.getActif() : true);

        //Save
        return categorieRepository.save(categorie);
    }
    // Mettre à jour une catégorie
    public Categorie updateCategorie(Long id, Categorie categorieDetails) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));

        // Vérifier doublon avec une autre catégorie
        categorieRepository.findByNom(categorieDetails.getNom())
                .filter(c -> !c.getIdCategorie().equals(id))
                .ifPresent(c -> {
                    throw new BadRequestException(
                        "Une autre catégorie avec le nom '" + c.getNom() + "' existe déjà."
                    );
                });

        categorie.setNom(categorieDetails.getNom());
        categorie.setDescription(categorieDetails.getDescription());
        categorie.setActif(categorieDetails.getActif());

        return categorieRepository.save(categorie);
    }

    
    // Supprimer une catégorie
    public void deleteCategorie(Long id) {
        Categorie categorie = getCategorieById(id);
        categorieRepository.delete(categorie);
    }

    @Transactional
    public void deleteAllByIds(List<Long> ids) {

        List<Categorie> categories = categorieRepository.findAllById(ids);

        if (categories.isEmpty()) {
            throw new RuntimeException("Aucune catégorie trouvée pour suppression");
        }

        categorieRepository.deleteAll(categories);
    }

}
