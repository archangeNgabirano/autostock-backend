package com.autostock_backend.autostock_backend.domain.dto;

import org.springframework.stereotype.Component;

import com.autostock_backend.autostock_backend.domain.entity.Piece;

@Component
public class PieceMapper {

     public PieceDto toDto(Piece piece) {
        return new PieceDto(
            piece.getIdPiece(),
            piece.getNom(),
            piece.getDescription(),
            piece.getPrixAchat(),
            piece.getPrixVente(),
            piece.getActif(),
            piece.getStockMin(),
            piece.getSousCategorie().getCategorie().getIdCategorie(),
            piece.getSousCategorie().getCategorie().getNom(),
            piece.getSousCategorie().getIdSousCategorie(),
            piece.getSousCategorie().getNom()
        );
    }
}

