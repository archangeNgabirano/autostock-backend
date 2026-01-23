// package com.autostock_backend.autostock_backend.service;


// import com.autostock_backend.autostock_backend.domain.entity.NumeroPiece;
// import com.autostock_backend.autostock_backend.repository.NumeroPieceRepository;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class NumeroPieceService {

//     private final NumeroPieceRepository repository;

//     public NumeroPieceService(NumeroPieceRepository repository) {
//         this.repository = repository;
//     }

//     public List<NumeroPiece> findAll() {
//         return repository.findAll();
//     }

//     public List<NumeroPiece> findBySousCategorie(Long idSousCategorie) {
//         return repository.findByIdSousCategorie(idSousCategorie);
//     }

//     public Optional<NumeroPiece> findById(Long id) {
//         return repository.findById(id);
//     }

//     public NumeroPiece save(NumeroPiece numeroPiece) {
//         return repository.save(numeroPiece);
//     }

//     public void delete(Long id) {
//         repository.deleteById(id);
//     }

// }

