package com.gmail.sergeymars8888.crm_system.mapper.impl;

import com.gmail.sergeymars8888.crm_system.dto.ContactDTO;
import com.gmail.sergeymars8888.crm_system.mapper.ContactMapper;
import com.gmail.sergeymars8888.crm_system.model.Contact;
import com.gmail.sergeymars8888.crm_system.model.Task;
import com.gmail.sergeymars8888.crm_system.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactMapperImpl implements ContactMapper {

    private final TaskRepository taskRepository;

    public ContactMapperImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ContactDTO toDto(Contact contact) {
        if (contact == null) {
            return null;
        }

        List<Long> taskIds = contact.getTasks().isEmpty() ? null : contact.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList());

        return new ContactDTO(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getClient() != null ? contact.getClient().getId() : null,
                taskIds
        );
    }

    @Override
    public Contact toEntity(ContactDTO contactDTO) {
        if (contactDTO == null) {
            return null;
        }

        Contact contact = new Contact();
        contact.setId(contactDTO.id());
        contact.setFirstName(contactDTO.firstName());
        contact.setLastName(contactDTO.lastName());
        contact.setEmail(contactDTO.email());
        contact.setPhone(contactDTO.phone());
        if (contactDTO.tasks() != null && !contactDTO.tasks().isEmpty()) {
            List<Task> tasks = contactDTO.tasks().stream()
                    .map(taskId -> taskRepository.findById(taskId)
                            .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " not found.")))
                    .collect(Collectors.toList());
            contact.setTasks(tasks);
        }
        return contact;
    }

}
