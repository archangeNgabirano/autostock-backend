package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.BadRequestException;
import com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised.NotFoundException;
import com.autostock_backend.autostock_backend.domain.dto.SousCategorieCreateDto;
import com.autostock_backend.autostock_backend.domain.entity.Categorie;
import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;
import com.autostock_backend.autostock_backend.repository.CategorieRepository;
import com.autostock_backend.autostock_backend.repository.SousCategorieRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class SousCategorieService {

    private final SousCategorieRepository sousCategorieRepository;
    private final CategorieRepository categorieRepository;

   

    public List<SousCategorie> findAll() {
        return sousCategorieRepository.findAll();
    }


    // Créer une sous-catégorie
public SousCategorie create(SousCategorieCreateDto dto) {

    if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
        throw new BadRequestException("Le nom de la sous-catégorie est obligatoire");
    }

    if (dto.getIdCategorie() == null) {
        throw new BadRequestException("La catégorie est obligatoire");
    }

    String nomNormalise = dto.getNom().trim();

    // Vérifier que la catégorie existe
    Categorie categorie = categorieRepository.findById(dto.getIdCategorie())
            .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));

    // Vérifier doublon
    boolean exists = sousCategorieRepository
        .existsByNomIgnoreCaseAndCategorie_IdCategorie(dto.getNom().trim(), dto.getIdCategorie());

if (exists) {
    throw new BadRequestException(
        "Une sous-catégorie avec ce nom existe déjà dans cette catégorie"
    );
}


    // Créer la sous-catégorie
    SousCategorie sc = new SousCategorie();
    sc.setNom(nomNormalise);
    sc.setDescription(dto.getDescription());
    sc.setActif(dto.getActif() != null ? dto.getActif() : true);
    sc.setCategorie(categorie); // ✅ set the entity directly

    return sousCategorieRepository.save(sc);
}


    
    // Mettre à jour une sous-catégorie

public SousCategorie updateSousCategorie(Long id, SousCategorieCreateDto dto) {
    SousCategorie existing = sousCategorieRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Sous-catégorie introuvable"));

    // Get the category entity
    Categorie categorie = categorieRepository.findById(dto.getIdCategorie())
            .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));

    // Check for duplicate name in the same category
    // boolean exists = sousCategorieRepository
    //         .existsByNomIgnoreCaseAndCategorie_IdCategorie(dto.getNom().trim(), dto.getIdCategorie());

    // if (exists && !existing.getNom().equalsIgnoreCase(dto.getNom().trim())) {
    //     throw new BadRequestException(
    //         "Une autre sous-catégorie avec ce nom existe déjà dans cette catégorie."
    //     );
    // }

    // Update fields
    existing.setNom(dto.getNom().trim());
    existing.setDescription(dto.getDescription());
    existing.setActif(dto.getActif() != null ? dto.getActif() : existing.getActif());
    existing.setCategorie(categorie);

    return sousCategorieRepository.save(existing);
}

 

    public void delete(Long id) {
        sousCategorieRepository.deleteById(id);
    }
}

