package com.autostock_backend.autostock_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.entity.Categorie;
import com.autostock_backend.autostock_backend.service.CategorieService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

@CrossOrigin("*") // autoriser Angular
@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    // GET : lister toutes les catégories
    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieService.getAllCategories();
    }

    // GET : récupérer une catégorie par ID
    @GetMapping("/{id}")
    public Categorie getCategorieById(@PathVariable Long id) {
        return categorieService.getCategorieById(id);
    }

    // POST : créer une seule catégorie
     @PostMapping
    public ResponseEntity<Categorie> create(@RequestBody Categorie categorie) {
        return ResponseEntity.ok(categorieService.createCategorie(categorie));
    }

    // PUT : mettre à jour une catégorie
    @PutMapping("/{id}")
    public Categorie updateCategorie(@PathVariable Long id, @RequestBody Categorie categorie) {
        return categorieService.updateCategorie(id, categorie);
    }

    // DELETE : supprimer une catégorie
    @DeleteMapping("/{id}")
    public void deleteCategorie(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
    }

    @PostMapping("/delete-multiple")
    public ResponseEntity<?> deleteCategories(@RequestBody List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("La liste des catégories à supprimer est vide");
        }

        categorieService.deleteAllByIds(ids);

        return ResponseEntity.ok("Catégories supprimées avec succès");
    }

}
