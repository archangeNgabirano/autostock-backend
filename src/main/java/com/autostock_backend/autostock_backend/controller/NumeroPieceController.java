// package com.autostock_backend.autostock_backend.controller;

// import com.autostock_backend.autostock_backend.domain.entity.NumeroPiece;
// import com.autostock_backend.autostock_backend.service.NumeroPieceService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/numero-pieces")
// @CrossOrigin("*")
// public class NumeroPieceController {

//     private final NumeroPieceService service;

//     public NumeroPieceController(NumeroPieceService service) {
//         this.service = service;
//     }

//     @GetMapping
//     public List<NumeroPiece> getAll() {
//         return service.findAll();
//     }

//     @GetMapping("/sous-categorie/{idSousCategorie}")
//     public List<NumeroPiece> getBySousCategorie(@PathVariable Long idSousCategorie) {
//         return service.findBySousCategorie(idSousCategorie);
//     }

//     @PostMapping
//     public ResponseEntity<NumeroPiece> create(@RequestBody NumeroPiece numeroPiece) {
//         return ResponseEntity.ok(service.save(numeroPiece));
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<NumeroPiece> update(@PathVariable Long id, @RequestBody NumeroPiece numeroPiece) {
//         numeroPiece.setIdNumeroPiece(id);
//         return ResponseEntity.ok(service.save(numeroPiece));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> delete(@PathVariable Long id) {
//         service.delete(id);
//         return ResponseEntity.noContent().build();
//     }
// }

