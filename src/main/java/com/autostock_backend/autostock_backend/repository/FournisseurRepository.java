package com.autostock_backend.autostock_backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.autostock_backend.autostock_backend.domain.entity.Fournisseur;
import java.util.List;


public interface FournisseurRepository extends JpaRepository<Fournisseur,Long>{
    List<Fournisseur> findByActifTrue();
}
