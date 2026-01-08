package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Categorie> creerCategories(List<Categorie> categories) {
        return categorieRepository.saveAll(categories);
    }

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
    public Categorie updateCategorie(Long id, Categorie categorieDetails) {
        Categorie categorie = getCategorieById(id);
        categorie.setNom(categorieDetails.getNom());
        categorie.setDescription(categorieDetails.getDescription());
        categorie.setActif(categorieDetails.getActif() );
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
