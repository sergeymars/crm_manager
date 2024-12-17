package com.gmail.sergeymars8888.crm_system.repository;

import com.gmail.sergeymars8888.crm_system.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByContactId(Long contactId);

}
