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
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.dto.PieceCreateUpdateDto;
import com.autostock_backend.autostock_backend.domain.dto.PieceDto;
import com.autostock_backend.autostock_backend.domain.dto.PieceMapper;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.service.PieceService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/pieces")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PieceController {

    private final PieceService pieceService;
    private final PieceMapper pieceMapper;

    @PostMapping
    public ResponseEntity<PieceDto> create(
            @RequestBody PieceCreateUpdateDto dto
    ) {
        Piece piece = pieceService.create(dto);
        return ResponseEntity.ok(pieceMapper.toDto(piece));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PieceDto> update(
            @PathVariable Long id,
            @RequestBody PieceCreateUpdateDto dto
    ) {
        Piece piece = pieceService.update(id, dto);
        return ResponseEntity.ok(pieceMapper.toDto(piece));
    }

    @GetMapping
    public List<PieceDto> getAll() {
        return pieceService.getAll()
                .stream()
                .map(pieceMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pieceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
