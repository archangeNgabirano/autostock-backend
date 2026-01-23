package com.autostock_backend.autostock_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autostock_backend.autostock_backend.domain.entity.ClientEntity;
import com.autostock_backend.autostock_backend.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    public List<ClientEntity> getClientsActifs() {
        return clientRepository.findByActifTrue();
    }

    public ClientEntity getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
    }

    public ClientEntity createClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity updateClient(Long id, ClientEntity clientDetails) {
        ClientEntity client = getClientById(id);
        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setContact(clientDetails.getContact());
        client.setActif(clientDetails.getActif());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.delete(getClientById(id));
    }

    public void deleteMultipleClients(List<Long> ids) {
        clientRepository.deleteAll(clientRepository.findAllById(ids));
    }
}

