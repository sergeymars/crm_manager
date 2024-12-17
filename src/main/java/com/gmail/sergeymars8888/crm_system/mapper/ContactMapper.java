package com.gmail.sergeymars8888.crm_system.mapper;

import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.model.Contact;


public interface ContactMapper {
    ContactDTO toDto(Contact contact);
    Contact toEntity(ContactDTO contactDTO);

}
