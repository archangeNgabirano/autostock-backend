package com.autostock_backend.autostock_backend.controller.PieceController;

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
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.service.PieceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pieces")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PieceController {

    private final PieceService pieceService;

    @PostMapping
    public ResponseEntity<Piece> create(@RequestBody Piece piece) {
        return ResponseEntity.ok(pieceService.create(piece));
    }

    @GetMapping
    public ResponseEntity<List<Piece>> getAll() {
        return ResponseEntity.ok(pieceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Piece> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pieceService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Piece> update(
            @PathVariable Long id,
            @RequestBody Piece piece) {
        return ResponseEntity.ok(pieceService.update(id, piece));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pieceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

