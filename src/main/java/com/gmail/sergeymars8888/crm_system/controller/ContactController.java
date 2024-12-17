package com.gmail.sergeymars8888.crm_system.controller;

import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.dto.TaskDTO;
import com.gmail.sergeymars8888.crm_system.mapper.ContactMapper;
import com.gmail.sergeymars8888.crm_system.model.Contact;
import com.gmail.sergeymars8888.crm_system.service.ContactService;
import com.gmail.sergeymars8888.crm_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;
    private final TaskService taskService;

    private final ContactMapper contactMapper;

    @Autowired
    public ContactController(ContactService contactService, TaskService taskService, ContactMapper contactMapper) {
        this.contactService = contactService;
        this.taskService = taskService;
        this.contactMapper = contactMapper;
    }

    @GetMapping
    public List<ContactDTO> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        try {
            ContactDTO contact = contactService.getContactById(id);
            return ResponseEntity.ok(contact);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO createdContact = contactService.createContact(contactDTO);
        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable Long id, @RequestBody ContactDTO contactDTO) {
        try {
            ContactDTO updatedContact = contactService.updateContact(id, contactDTO);
            return ResponseEntity.ok(updatedContact);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        try {
            contactService.deleteContact(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{contactId}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByContactId(@PathVariable Long contactId) {
        List<TaskDTO> tasks = taskService.getTasksByContactId(contactId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactDTO>> searchContacts(@RequestParam(required = false) String firstName,
                                                           @RequestParam(required = false) String lastName,
                                                           @RequestParam(required = false) String email,
                                                           @RequestParam(required = false) String phone,
                                                           @RequestParam(required = false) Long clientId) {
        List<Contact> contacts = contactService.searchContacts(firstName, lastName, email, phone, clientId);
        List<ContactDTO> contactDTOs = new ArrayList<>();
        for (Contact contact : contacts) {
            ContactDTO contactDTO = contactMapper.toDto(contact);
            contactDTOs.add(contactDTO);
        }
        return ResponseEntity.ok(contactDTOs);
    }
}
