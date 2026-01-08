package com.autostock_backend.autostock_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByPieceIdPieceAndEntrepotIdEntrepot(
            Piece piece,
            Entrepot entrepot);

Optional<Stock> findByPieceIdPieceAndEntrepotIdEntrepot(Long idPiece, Long idEntrepot);

    List<Stock> findByActifTrue();
}
