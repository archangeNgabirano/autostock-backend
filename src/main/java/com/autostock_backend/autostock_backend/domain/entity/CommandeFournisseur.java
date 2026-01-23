package com.autostock_backend.autostock_backend.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.autostock_backend.autostock_backend.domain.enums.StatutCommande;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "commandes_fournisseur")
public class CommandeFournisseur {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idCommande;


private LocalDate dateCommande;
private LocalDate dateLivraisonPrevue;
@Enumerated(EnumType.STRING)
private StatutCommande statut;
private BigDecimal montantTotal;


@ManyToOne
@JoinColumn(name = "idFournisseur",insertable = false,updatable = false)
private Fournisseur fournisseur;
private Long idFournisseur;

}
