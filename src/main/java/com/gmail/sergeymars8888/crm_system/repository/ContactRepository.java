package com.gmail.sergeymars8888.crm_system.repository;

import com.gmail.sergeymars8888.crm_system.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    List<Contact> findByClientId(Long clientId);
}
