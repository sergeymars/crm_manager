package com.gmail.sergeymars8888.crm_system.service.impl;

import com.gmail.sergeymars8888.crm_system.dto.TaskDTO;
import com.gmail.sergeymars8888.crm_system.mapper.TaskMapper;
import com.gmail.sergeymars8888.crm_system.model.Contact;
import com.gmail.sergeymars8888.crm_system.model.Task;
import com.gmail.sergeymars8888.crm_system.repository.ContactRepository;
import com.gmail.sergeymars8888.crm_system.repository.TaskRepository;
import com.gmail.sergeymars8888.crm_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final ContactRepository contactRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    @Autowired
    public TaskServiceImpl(ContactRepository contactRepository, TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.contactRepository = contactRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public List<TaskDTO> getAllTasks() {

        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
        return taskDTOs;
    }


    @Transactional(readOnly = true)
    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDto(task);
    }


    @Transactional
    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }


    @Transactional
    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with id " + id + " not found"));

        existingTask.setDescription(taskDTO.description());
        existingTask.setStatus(taskDTO.status());
        existingTask.setDueDate(taskDTO.dueDate());

        if (taskDTO.contactId() != null) {
            Contact contact = contactRepository.findById(taskDTO.contactId())
                    .orElseThrow(() -> new RuntimeException("Contact with id " + taskDTO.contactId() + " not found"));
            existingTask.setContact(contact);
        }
        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toDto(updatedTask);
    }


    @Transactional
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskDTO> getTasksByContactId(Long contactId) {
        List<Task> tasks = taskRepository.findByContactId(contactId);
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

}
