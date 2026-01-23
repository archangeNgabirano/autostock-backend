package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.Entrepot;
import com.autostock_backend.autostock_backend.repository.EntrepotRepository;

@Service
public class EntrepotService {

    @Autowired
    EntrepotRepository entrepotRepository;

    public List<Entrepot> getAllEntrepot(){
        return entrepotRepository.findAll();
    }

    public Entrepot getByEntrepotId(Long id){
        return entrepotRepository.findById(id).orElseThrow(()->new RuntimeException ("'Not found Id'"));
    }


    // Nouvelle méthode pour entrepôts actifs
    public List<Entrepot> getActifEntrepot() {
        return entrepotRepository.findByActifTrue();
    }

    public Entrepot createEntrepot(Entrepot entrepot){
        return entrepotRepository.save(entrepot);
    }

    public Entrepot updateEntrepot(Long id,Entrepot entrepotDetails){

        Entrepot entrepot =getByEntrepotId(id);
        entrepot.setLocalisation(entrepotDetails.getLocalisation());
        entrepot.setNom(entrepotDetails.getNom());
        entrepot.setActif(entrepotDetails.getActif());
        entrepot.setTypeEntrepot(entrepotDetails.getTypeEntrepot());
        return entrepotRepository.save(entrepot);
    }

    public void deleteEntrepot(Long id){

        Entrepot entrepot=getByEntrepotId(id);
        entrepotRepository.delete(entrepot);
    }

    public void deleteMultipleEntrpot(List<Long> ids){
      List<Entrepot> entrepots=entrepotRepository.findAllById(ids);

      if (entrepots.isEmpty()) {
       throw new RuntimeException("Liste vide");
      }
      entrepotRepository.deleteAll(entrepots);
    }

}
