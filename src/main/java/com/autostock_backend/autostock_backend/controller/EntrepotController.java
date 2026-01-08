package com.autostock_backend.autostock_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.service.EntrepotService;

import java.util.List;

@RestController
@RequestMapping("/api/entrepots")
@CrossOrigin("*") // autoriser Angular

public class EntrepotController {

    @Autowired
    private EntrepotService entrepotService;

    // -------------------------
    // GET : Tous les entrepots
    // -------------------------
    @GetMapping
    public List<Entrepot> getAllEntrepots() {
        return entrepotService.getAllEntrepot();
    }

    // GET : entrepôts actifs
    @GetMapping("/actifs")
    public List<Entrepot> getActifEntrepots() {
        return entrepotService.getActifEntrepot();
    }

    // -------------------------
    // GET : Un entrepot par ID
    // -------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Entrepot> getEntrepotById(@PathVariable Long id) {
        Entrepot entrepot = entrepotService.getByEntrepotId(id);
        return ResponseEntity.ok(entrepot);
    }

    // -------------------------
    // POST : Créer un entrepot
    // -------------------------
    @PostMapping
    public Entrepot createEntrepot(@RequestBody Entrepot entrepot) {
        return entrepotService.createEntrepot(entrepot);
    }

    // -------------------------
    // PUT : Mettre à jour un entrepot
    // -------------------------
    @PutMapping("/{id}")
    public Entrepot updateEntrepot(@PathVariable Long id, @RequestBody Entrepot entrepotDetails) {
        return entrepotService.updateEntrepot(id, entrepotDetails);
    }

    // -------------------------
    // DELETE : Supprimer un entrepot
    // -------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------
    // DELETE : Supprimer plusieurs entrepots
    // -------------------------
    @DeleteMapping
    public ResponseEntity<Void> deleteMultipleEntrepots(@RequestBody List<Long> ids) {
        entrepotService.deleteMultipleEntrpot(ids);
        return ResponseEntity.noContent().build();
    }
}

