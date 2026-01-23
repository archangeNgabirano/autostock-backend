package com.autostock_backend.autostock_backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.JournalAudit;

public interface JournalAuditRepository extends JpaRepository<JournalAudit,Long> {
List<JournalAudit> findByDateActionBetween(LocalDateTime debut, LocalDateTime fin);
}
