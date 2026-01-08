package com.autostock_backend.autostock_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.dto.StockRequestDTO;
import com.autostock_backend.autostock_backend.domain.entity.Stock;
import com.autostock_backend.autostock_backend.domain.entity.StockAudit;
import com.autostock_backend.autostock_backend.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StockController {

    private final StockService stockService;

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody StockRequestDTO dto) {
        return ResponseEntity.ok(stockService.createOrUpdateStock(dto));
    }

    // ---------------- READ ALL ----------------
    @GetMapping
    public ResponseEntity<List<Stock>> findAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

    // ---------------- READ BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Stock> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(
            @PathVariable Long id,
            @RequestBody StockRequestDTO dto
    ) {
        return ResponseEntity.ok(stockService.update(id, dto));
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) Long idUtilisateur // facultatif
    ) {
        stockService.delete(id, idUtilisateur);
        return ResponseEntity.noContent().build();
    }

    // ---------------- AUDIT ----------------
    @GetMapping("/{id}/audit")
    public ResponseEntity<List<StockAudit>> audit(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getAudit(id));
    }
}
