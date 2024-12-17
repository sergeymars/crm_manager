package com.gmail.sergeymars8888.crm_system.service;

import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.model.Contact;

import java.util.List;

public interface ContactService {
    List<ContactDTO> getAllContacts();

    ContactDTO getContactById(Long id);

    ContactDTO createContact(ContactDTO contactDTO);

    ContactDTO updateContact(Long id, ContactDTO contactDTO);

    void deleteContact(Long id);

    List<Contact> searchContacts(String firstName, String lastName, String email, String phone, Long clientId);

}
