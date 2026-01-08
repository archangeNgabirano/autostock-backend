package com.autostock_backend.autostock_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.autostock_backend.autostock_backend.domain.entity.MouvementStock;

public interface MouvementStockRepository extends JpaRepository<MouvementStock,Long> {


List<MouvementStock> findByIdPiece(Long id);


}
