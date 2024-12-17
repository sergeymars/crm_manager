package com.gmail.sergeymars8888.crm_system.service;

import com.gmail.sergeymars8888.crm_system.dto.ClientDTO;
import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.model.Client;

import java.util.List;

public interface ClientService {
    ClientDTO getClientById(Long id);

    List<ClientDTO> getAllClients();

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(Long id, ClientDTO clientDTO);

    Boolean deleteClient(Long id);

    List<ContactDTO> getContactsWithTasksByClientId(Long clientId);

    List<Client> searchClients(String name, String industry, String address);

}
