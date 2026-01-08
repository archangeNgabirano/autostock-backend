package com.autostock_backend.autostock_backend.service;

import java.time.LocalDateTime;

import com.autostock_backend.autostock_backend.domain.entity.JournalAudit;
import com.autostock_backend.autostock_backend.domain.entity.Piece;
import com.autostock_backend.autostock_backend.domain.entity.Utilisateur;


public final class AuditFactory {

    private AuditFactory() {
        // empêche l’instanciation
    }

    public static JournalAudit creer(
            String action,
            Piece piece,
            Utilisateur utilisateur
    ) {
        JournalAudit audit = new JournalAudit();
        audit.setAction(action);
        audit.setEntite("PIECE");
        audit.setIdentifiantEntite(piece.getIdPiece());
        audit.setIdUtilisateur(utilisateur.getIdUtilisateur());
        audit.setDateAction(LocalDateTime.now());

        return audit;
    }
}

