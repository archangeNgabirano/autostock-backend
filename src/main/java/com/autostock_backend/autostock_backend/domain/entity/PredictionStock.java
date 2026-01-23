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
@Table(name = "predictions_stock")
public class PredictionStock {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idPredictionStock;
private String periode;
private Long demandePrevue;
private LocalDateTime dateCalcul;
private String methode;


@ManyToOne
@JoinColumn(name = "idPiece",insertable = false,updatable = false)
private Piece piece;
private Long idPiece;
}
