package com.gmail.sergeymars8888.crm_system.mapper.impl;

import com.gmail.sergeymars8888.crm_system.dto.ClientDTO;
import com.gmail.sergeymars8888.crm_system.mapper.ClientMapper;
import com.gmail.sergeymars8888.crm_system.model.Client;
import com.gmail.sergeymars8888.crm_system.model.Contact;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientDTO toDto(Client client) {
        if (client == null) {
            return null;
        }

        List<Long> contactIds = client.getContacts().stream()
                .map(Contact::getId)
                .collect(Collectors.toList());

        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getIndustry(),
                client.getAddress(),
                contactIds
        );
    }

    @Override
    public Client toEntity(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }

        Client client = new Client();
        client.setId(clientDTO.id());
        client.setName(clientDTO.name());
        client.setIndustry(clientDTO.industry());
        client.setAddress(clientDTO.address());

        return client;
    }
}
