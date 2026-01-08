package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;
import com.autostock_backend.autostock_backend.repository.SousCategorieRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SousCategorieService {

    private final SousCategorieRepository repository;

    public SousCategorieService(SousCategorieRepository repository) {
        this.repository = repository;
    }

    public List<SousCategorie> findAll() {
        return repository.findAll();
    }

    public SousCategorie save(SousCategorie sc) {
        if (sc.getIdCategorie() == null) {
            throw new RuntimeException("Category is required");
        }
        return repository.save(sc);
    }

    public SousCategorie update(Long id, SousCategorie sc) {
        SousCategorie existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SousCategorie not found"));

        existing.setNom(sc.getNom());
        existing.setDescription(sc.getDescription());
        existing.setActif(sc.getActif());
        existing.setIdCategorie(sc.getIdCategorie());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

