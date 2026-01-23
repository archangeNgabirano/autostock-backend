package com.autostock_backend.autostock_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.dto.StockAjustementRequest;
import com.autostock_backend.autostock_backend.domain.dto.StockCreateRequest;
import com.autostock_backend.autostock_backend.domain.dto.StockTransfertRequest;
import com.autostock_backend.autostock_backend.domain.entity.Stock;
import com.autostock_backend.autostock_backend.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StockController {

    private final StockService stockService;

    // ================= APPROVISIONNEMENT =================
    @PostMapping("/approvisionnement")
    public ResponseEntity<Stock> approvisionner(
            @RequestBody StockCreateRequest request) {
        return ResponseEntity.ok(
                stockService.enregistrerApprovisionnement(request));
    }

    // ================= SORTIE =================
    // @PostMapping("/sortie")
    // public ResponseEntity<Stock> sortie(
    // @RequestBody StockSortieRequest request) {
    // return ResponseEntity.ok(
    // stockService.sortieStock(request)
    // );
    // }

    // ================= AJUSTEMENT =================
    @PostMapping("/ajustement")
    public ResponseEntity<Stock> ajustement(
            @RequestBody StockAjustementRequest request) {
        return ResponseEntity.ok(
                stockService.ajusterStock(request));
    }

    // ================= TRANSFERT =================
    @PostMapping("/transfert")
    public ResponseEntity<Void> transferer(@RequestBody StockTransfertRequest request) {
        stockService.transfererStock(request);
        return ResponseEntity.ok().build();
    }

    // ================= DESACTIVER =================
    @DeleteMapping("/{idStock}/utilisateur/{idUtilisateur}")
    public ResponseEntity<Void> desactiver(
            @PathVariable Long idStock,
            @PathVariable Long idUtilisateur) {
        stockService.desactiverStock(idStock, idUtilisateur);
        return ResponseEntity.noContent().build();
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<Stock>> findAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }
}
