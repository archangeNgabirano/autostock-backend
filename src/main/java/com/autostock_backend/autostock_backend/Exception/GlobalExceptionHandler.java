package com.autostock_backend.autostock_backend.Exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gestion de toutes les ApiException
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", ex.getStatus());
        error.put("timestamp", Instant.now());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    // Gestion des NullPointerException ou autres erreurs internes
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Erreur serveur : " + ex.getMessage());
        error.put("status", 500);
        error.put("timestamp", Instant.now());
        return ResponseEntity.status(500).body(error);
    }
}

