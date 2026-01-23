package com.autostock_backend.autostock_backend.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "journal_audit")
public class JournalAudit {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idJournalAudite;

private String action;
private String entite;
private Long identifiantEntite;
private LocalDateTime dateAction;


@ManyToOne
@JoinColumn(name = "IdUtilisateur",insertable = false,updatable  = false)
private Utilisateur utilisateur;
private Long IdUtilisateur;
}
