package com.gmail.sergeymars8888.crm_system.service.impl;

import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.mapper.ContactMapper;
import com.gmail.sergeymars8888.crm_system.model.Client;
import com.gmail.sergeymars8888.crm_system.model.Contact;
import com.gmail.sergeymars8888.crm_system.model.Task;
import com.gmail.sergeymars8888.crm_system.repository.ClientRepository;
import com.gmail.sergeymars8888.crm_system.repository.ContactRepository;
import com.gmail.sergeymars8888.crm_system.repository.TaskRepository;
import com.gmail.sergeymars8888.crm_system.service.ContactService;
import com.gmail.sergeymars8888.crm_system.specification.ContactSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public ContactServiceImpl(ClientRepository clientRepository, ContactRepository contactRepository, ContactMapper contactMapper, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContactDTO> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return contacts.stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ContactDTO getContactById(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            return contactMapper.toDto(contact.get());
        } else {
            throw new RuntimeException("Contact with id " + id + " not found.");
        }
    }


    @Transactional
    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contact = contactMapper.toEntity(contactDTO);
        contact = contactRepository.save(contact);

        if (contactDTO.tasks() != null && !contactDTO.tasks().isEmpty()) {
            List<Task> tasks = contactDTO.tasks().stream()
                    .map(taskId -> taskRepository.findById(taskId)
                            .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " not found.")))
                    .collect(Collectors.toList());

            for (Task task : tasks) {
                task.setContact(contact);
            }
            taskRepository.saveAll(tasks);
        }

        return contactMapper.toDto(contact);
    }


    @Transactional
    @Override
    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Optional<Contact> existingContact = contactRepository.findById(id);
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.setFirstName(contactDTO.firstName());
            contact.setLastName(contactDTO.lastName());
            contact.setEmail(contactDTO.email());
            contact.setPhone(contactDTO.phone());

            if (contactDTO.clientId() != null && !contactDTO.clientId().equals(contact.getClient().getId())) {
                Client newClient = clientRepository.findById(contactDTO.clientId())
                        .orElseThrow(() -> new RuntimeException("Client with id " + contactDTO.clientId() + " not found."));

                Client oldClient = contact.getClient();
                oldClient.getContacts().remove(contact);
                clientRepository.save(oldClient);

                contact.setClient(newClient);
                newClient.getContacts().add(contact);
                clientRepository.save(newClient);
            }
            contact = contactRepository.save(contact);
            return contactMapper.toDto(contact);
        } else {
            throw new RuntimeException("Contact with id " + id + " not found.");
        }
    }

    @Transactional
    @Override
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact with id " + id + " not found."));

        List<Task> tasks = taskRepository.findByContactId(id);
        for (Task task : tasks) {
            task.setContact(null);
            taskRepository.save(task);
        }
        contactRepository.delete(contact);
    }


    @Transactional(readOnly = true)
    @Override
    public List<Contact> searchContacts(String firstName, String lastName, String email, String phone, Long clientId) {
        Specification<Contact> spec = Specification.where(ContactSpecification.hasFirstName(firstName))
                .and(ContactSpecification.hasLastName(lastName))
                .and(ContactSpecification.hasEmail(email))
                .and(ContactSpecification.hasPhone(phone))
                .and(ContactSpecification.hasClientId(clientId));
        return contactRepository.findAll(spec);
    }
}
