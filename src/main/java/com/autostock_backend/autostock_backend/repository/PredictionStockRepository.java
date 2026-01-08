package com.autostock_backend.autostock_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.PredictionStock;

public interface PredictionStockRepository extends JpaRepository<PredictionStock,Long> {
List<PredictionStock> findByIdPiece(Long idPiece);
}
