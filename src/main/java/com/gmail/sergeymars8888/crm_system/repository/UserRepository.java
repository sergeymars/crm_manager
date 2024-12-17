package com.gmail.sergeymars8888.crm_system.repository;

import com.gmail.sergeymars8888.crm_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
