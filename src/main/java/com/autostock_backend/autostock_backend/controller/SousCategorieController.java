package com.autostock_backend.autostock_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.entity.SousCategorie;
import com.autostock_backend.autostock_backend.service.SousCategorieService;

@RestController
@RequestMapping("/api/sous-categories")
@CrossOrigin("*")
public class SousCategorieController {

    private final SousCategorieService service;

    public SousCategorieController(SousCategorieService service) {
        this.service = service;
    }

    @GetMapping
    public List<SousCategorie> getAll() {
        return service.findAll();
    }

    @PostMapping
    public SousCategorie create(@RequestBody SousCategorie sc) {
        return service.save(sc);
    }

    @PutMapping("/{id}")
    public SousCategorie update(@PathVariable Long id, @RequestBody SousCategorie sc) {
        return service.update(id, sc);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
