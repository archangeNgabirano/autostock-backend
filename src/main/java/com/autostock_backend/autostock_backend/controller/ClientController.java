package com.autostock_backend.autostock_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autostock_backend.autostock_backend.domain.entity.ClientEntity;
import com.autostock_backend.autostock_backend.service.ClientService;


@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*") // autoriser Angular@CrossOrigin("*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<ClientEntity> getAll() {
        return clientService.getAllClients();
    }

    @GetMapping("/actifs")
    public List<ClientEntity> getActifs() {
        return clientService.getClientsActifs();
    }

    @GetMapping("/{id}")
    public ClientEntity getById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PostMapping
    public ClientEntity create(@RequestBody ClientEntity client) {
        return clientService.createClient(client);
    }

    @PutMapping("/{id}")
    public ClientEntity update(@PathVariable Long id, @RequestBody ClientEntity client) {
        return clientService.updateClient(id, client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.deleteClient(id);
    }

    @DeleteMapping
    public void deleteMultiple(@RequestBody List<Long> ids) {
        clientService.deleteMultipleClients(ids);
    }
}

