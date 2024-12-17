package com.gmail.sergeymars8888.crm_system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.sergeymars8888.crm_system.dto.ClientDTO;
import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.mapper.ClientMapper;
import com.gmail.sergeymars8888.crm_system.mapper.ContactMapper;
import com.gmail.sergeymars8888.crm_system.model.Client;
import com.gmail.sergeymars8888.crm_system.model.Contact;
import com.gmail.sergeymars8888.crm_system.model.Task;
import com.gmail.sergeymars8888.crm_system.repository.ClientRepository;
import com.gmail.sergeymars8888.crm_system.repository.ContactRepository;
import com.gmail.sergeymars8888.crm_system.repository.TaskRepository;
import com.gmail.sergeymars8888.crm_system.service.ClientService;
import com.gmail.sergeymars8888.crm_system.specification.ClientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;
    private final TaskRepository taskRepository;
    private final ClientMapper clientMapper;
    private final ContactMapper contactMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;


    @Autowired
    public ClientServiceImpl(ObjectMapper objectMapper, RedisTemplate<String, String> redisTemplate, ClientRepository clientRepository, ContactRepository contactRepository, TaskRepository taskRepository, ClientMapper clientMapper, ContactMapper contactMapper) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
        this.taskRepository = taskRepository;
        this.clientMapper = clientMapper;
        this.contactMapper = contactMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return clientMapper.toDto(client);
    }

    @Cacheable(value = "clients", key = "'allClients'", unless = "#result == null")
    @Transactional(readOnly = true)
    @Override
    public List<ClientDTO> getAllClients() {
        String cacheKey = "clients";
        String cachedData = redisTemplate.opsForValue().get(cacheKey);
        if (cachedData == null) {
            List<Client> clients = clientRepository.findAll();
            try {
                String json = objectMapper.writeValueAsString(clients);
                redisTemplate.opsForValue().set(cacheKey, json);
            } catch (IOException e) {
                throw new RuntimeException("Error serializing client list to JSON", e);
            }
            return clients.stream()
                    .map(clientMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            try {
                List<Client> clients = objectMapper.readValue(cachedData, objectMapper.getTypeFactory().constructCollectionType(List.class, Client.class));
                return clients.stream()
                        .map(clientMapper::toDto)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException("Error deserializing client list from JSON", e);
            }
        }
    }

    @Transactional
    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        if (clientDTO.contactIds() != null && !clientDTO.contactIds().isEmpty()) {
            List<Contact> contacts = contactRepository.findAllById(clientDTO.contactIds());
            for (Contact contact : contacts) {
                contact.setClient(client);
            }
            client.setContacts(contacts);
        }
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Transactional
    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        existingClient.setName(clientDTO.name());
        existingClient.setIndustry(clientDTO.industry());
        existingClient.setAddress(clientDTO.address());

        List<Long> updatedContactIds = clientDTO.contactIds();

        if (updatedContactIds != null && !updatedContactIds.isEmpty()) {
            List<Contact> updatedContacts = contactRepository.findAllById(updatedContactIds);
            if (updatedContacts.size() != updatedContactIds.size()) {
                throw new RuntimeException("Some contact IDs not found");
            }
            updatedContacts.forEach(contact -> contact.setClient(existingClient));
            contactRepository.saveAll(updatedContacts);
        }

        Client updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }

    @Transactional
    @Override
    public Boolean deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client with id " + id + " not found.");
        } else {

            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            client.getContacts().forEach(contact -> {
                List<Task> tasks = taskRepository.findByContactId(contact.getId());
                if (!tasks.isEmpty()) {
                    tasks.forEach(task -> task.setContact(null));
                    taskRepository.saveAll(tasks);
                }
            });
            clientRepository.deleteById(id);
            return true;
        }


    }

    @Override
    public List<ContactDTO> getContactsWithTasksByClientId(Long clientId) {
        List<Contact> contacts = contactRepository.findByClientId(clientId);
        return contacts.stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<Client> searchClients(String name, String industry, String address) {
        Specification<Client> spec = Specification.where(ClientSpecification.hasName(name))
                .and(ClientSpecification.hasIndustry(industry))
                .and(ClientSpecification.hasAddress(address));
        return clientRepository.findAll(spec);
    }

}
