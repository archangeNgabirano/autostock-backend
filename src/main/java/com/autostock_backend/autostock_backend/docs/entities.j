package com.autostock_backend.autostock_backend.docs;

// ==============================
// PACKAGE : domain.entity
// ==============================

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ------------------------------
// CATEGORIE
// ------------------------------
@Entity
@Table(name = "categories")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String description;

    private Boolean actif = true;

    @OneToMany(mappedBy = "categorie")
    private List<SousCategorie> sousCategories;
}

// ------------------------------
// SOUS CATEGORIE
// ------------------------------
@Entity
@Table(name = "sous_categories")
public class SousCategorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;

    private Boolean actif = true;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "sousCategorie")
    private List<Piece> pieces;
}

// ------------------------------
// PIECE
// ------------------------------
@Entity
@Table(name = "pieces")
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_piece", nullable = false, unique = true)
    private String numeroPiece;

    private String nom;
    private String description;

    private Integer stockActuel;
    private Integer stockMin;
    private Integer stockMax;

    private Integer delaiReapprovisionnement;

    private Double indiceRotation;
    private Double scoreRisqueRupture;

    private BigDecimal prixAchat;
    private BigDecimal prixVente;

    private Boolean actif = true;

    @ManyToOne
    @JoinColumn(name = "sous_categorie_id", nullable = false)
    private SousCategorie sousCategorie;
}

// ------------------------------
// MOUVEMENT DE STOCK
// ------------------------------
@Entity
@Table(name = "mouvements_stock")
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeMouvement typeMouvement;

    private Integer quantite;

    private LocalDateTime dateMouvement;

    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "piece_id")
    private Piece piece;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}

// ------------------------------
// ENTREPOT
// ------------------------------
@Entity
@Table(name = "entrepots")
public class Entrepot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String localisation;
}

// ------------------------------
// FOURNISSEUR
// ------------------------------
@Entity
@Table(name = "fournisseurs")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String contact;

    private Integer delaiLivraisonMoyen;
    private Double noteFiabilite;
}

// ------------------------------
// COMMANDE FOURNISSEUR
// ------------------------------
@Entity
@Table(name = "commandes_fournisseur")
public class CommandeFournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateCommande;
    private LocalDate dateLivraisonPrevue;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    private BigDecimal montantTotal;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commandeFournisseur", cascade = CascadeType.ALL)
    private List<LigneCommandeFournisseur> lignes;
}

// ------------------------------
// LIGNE COMMANDE FOURNISSEUR
// ------------------------------
@Entity
@Table(name = "ligne_commande_fournisseur")
public class LigneCommandeFournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantiteCommandee;
    private BigDecimal prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "piece_id")
    private Piece piece;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private CommandeFournisseur commandeFournisseur;
}

// ------------------------------
// UTILISATEUR
// ------------------------------
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true)
    private String email;

    private String motDePasse;

    private Boolean actif = true;
}

// ------------------------------
// JOURNAL AUDIT
// ------------------------------
@Entity
@Table(name = "journal_audit")
public class JournalAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String entite;
    private Long identifiantEntite;

    private LocalDateTime dateAction;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}

// ------------------------------
// PREDICTION STOCK
// ------------------------------
@Entity
@Table(name = "predictions_stock")
public class PredictionStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String periode;
    private Integer demandePrevue;
    private LocalDateTime dateCalcul;
    private String methode;

    @ManyToOne
    @JoinColumn(name = "piece_id")
    private Piece piece;
}

// ==============================
// ENUMS
// ==============================

enum TypeMouvement {
    ENTREE,
    SORTIE,
    TRANSFERT,
    AJUSTEMENT
}

enum StatutCommande {
    EN_COURS,
    LIVREE,
    ANNULEE
}

