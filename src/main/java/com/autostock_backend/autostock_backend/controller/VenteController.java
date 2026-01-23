package com.autostock_backend.autostock_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.entity.Vente;
import com.autostock_backend.autostock_backend.service.VenteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    /* ===== CREER ===== */
    @PostMapping
    public ResponseEntity<Vente> creer(@RequestBody Vente vente) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(venteService.creerVente(vente));
    }

    /* ===== ANNULER ===== */
    // @PostMapping("/{id}/annuler")
    // public ResponseEntity<Vente> annuler(
    //         @PathVariable Long id,
    //         @RequestParam String motif
    // ) {
    //     return ResponseEntity.ok(venteService.annulerVente(id, motif));
    // }

    /* ===== GET ALL ===== */
    @GetMapping
    public ResponseEntity<List<Vente>> getAll() {
        return ResponseEntity.ok(venteService.getAll());
    }

    /* ===== GET BY ID ===== */
    @GetMapping("/{id}")
    public ResponseEntity<Vente> getById(@PathVariable Long id) {
        return ResponseEntity.ok(venteService.getById(id));
    }

    /* ===== GET BY NUMERO ===== */
    @GetMapping("/numero/{numero}")
    public ResponseEntity<Vente> getByNumero(@PathVariable String numero) {
        return ResponseEntity.ok(venteService.getByNumero(numero));
    }
}
