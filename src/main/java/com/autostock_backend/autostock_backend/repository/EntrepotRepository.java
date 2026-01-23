package com.autostock_backend.autostock_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.Entrepot;

public interface EntrepotRepository extends JpaRepository<Entrepot,Long>{
    List<Entrepot> findByActifTrue();
    Optional<Entrepot> findByIdEntrepotAndActifTrue(Long idEntrepot);

    List<Entrepot> findAllByActifTrue();

}
