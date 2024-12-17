package com.gmail.sergeymars8888.crm_system.mapper;


import com.gmail.sergeymars8888.crm_system.dto.ClientDTO;
import com.gmail.sergeymars8888.crm_system.model.Client;


public interface ClientMapper {
    ClientDTO toDto(Client client);
    Client toEntity(ClientDTO clientDTO);
}
