package com.gmail.sergeymars8888.crm_system.controller;


import com.gmail.sergeymars8888.crm_system.dto.ClientDTO;
import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.mapper.ClientMapper;
import com.gmail.sergeymars8888.crm_system.model.Client;
import com.gmail.sergeymars8888.crm_system.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientMapper clientMapper;
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientMapper clientMapper, ClientService clientService) {
        this.clientMapper = clientMapper;
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        ClientDTO createdClient = clientService.createClient(clientDTO);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return updatedClient != null ? ResponseEntity.ok(updatedClient) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        boolean isDeleted = clientService.deleteClient(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{clientId}/contacts-with-tasks")
    public ResponseEntity<List<ContactDTO>> getContactsWithTasksByClientId(@PathVariable Long clientId) {
        List<ContactDTO> contactsWithTasks = clientService.getContactsWithTasksByClientId(clientId);
        return ResponseEntity.ok(contactsWithTasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientDTO>> searchClients(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String industry,
                                                         @RequestParam(required = false) String address) {
        List<Client> clients = clientService.searchClients(name, industry, address);
        List<ClientDTO> clientDTOs = new ArrayList<>();
        for (Client client : clients) {
            ClientDTO clientDTO = clientMapper.toDto(client);
            clientDTOs.add(clientDTO);
        }
        return ResponseEntity.ok(clientDTOs);
    }
}

