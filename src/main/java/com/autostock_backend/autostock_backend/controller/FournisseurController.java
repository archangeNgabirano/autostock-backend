package com.autostock_backend.autostock_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autostock_backend.autostock_backend.domain.entity.Fournisseur;
import com.autostock_backend.autostock_backend.service.FournisseurService;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@CrossOrigin("*")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    // ---------------- GET ALL ----------------
    @GetMapping
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurService.getAllFournisseurs();
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Fournisseur> getFournisseurById(@PathVariable Long id) {
        return fournisseurService.getFournisseurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public Fournisseur createFournisseur(@RequestBody Fournisseur fournisseur) {
        return fournisseurService.saveFournisseur(fournisseur);
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> updateFournisseur(@PathVariable Long id,
                                                         @RequestBody Fournisseur fournisseurDetails) {
        return fournisseurService.getFournisseurById(id)
                .map(existing -> {
                    existing.setNom(fournisseurDetails.getNom());
                    existing.setPrenom(fournisseurDetails.getPrenom());
                    existing.setContact(fournisseurDetails.getContact());
                    existing.setActif(fournisseurDetails.getActif());
                    Fournisseur updated = fournisseurService.saveFournisseur(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        return fournisseurService.getFournisseurById(id)
                .map(f -> {
                    fournisseurService.deleteFournisseur(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

