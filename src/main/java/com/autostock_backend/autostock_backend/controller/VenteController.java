package com.autostock_backend.autostock_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.dto.VenteCreateRequest;
import com.autostock_backend.autostock_backend.domain.dto.VenteMapper;
import com.autostock_backend.autostock_backend.domain.dto.VenteResponseDto;
import com.autostock_backend.autostock_backend.domain.entity.Vente;
import com.autostock_backend.autostock_backend.service.VenteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    // Créer une vente
    @PostMapping
    public ResponseEntity<VenteResponseDto> creerVente(@RequestBody VenteCreateRequest request) {
        VenteResponseDto response = venteService.creerVente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Annuler une vente
    @PostMapping("/{id}/annuler")
    public ResponseEntity<VenteResponseDto> annulerVente(
            @PathVariable Long id,
            @RequestParam String motif) {

        Vente vente = venteService.annulerVente(id, motif);
        return ResponseEntity.ok(VenteMapper.toResponseDto(vente));
    }

    // Liste de toutes les ventes
    @GetMapping
    public ResponseEntity<List<VenteResponseDto>> getAllVentes() {
        List<VenteResponseDto> ventes = venteService.getAll().stream()
                .map(VenteMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(ventes);
    }

    // Vente par ID
    @GetMapping("/{id}")
    public ResponseEntity<VenteResponseDto> getVenteById(@PathVariable Long id) {
        Vente vente = venteService.getById(id);
        return ResponseEntity.ok(VenteMapper.toResponseDto(vente));
    }

    // Vente par numéro
    @GetMapping("/numero/{numero}")
    public ResponseEntity<VenteResponseDto> getByNumero(@PathVariable String numero) {
        return ResponseEntity.ok(venteService.getByNumero(numero));
    }
}
