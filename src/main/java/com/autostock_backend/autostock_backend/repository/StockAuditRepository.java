package com.autostock_backend.autostock_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.StockAudit;

public interface StockAuditRepository extends JpaRepository<StockAudit, Long> {

    List<StockAudit> findByStockIdStockOrderByDateActionDesc(Long idStock);

}
