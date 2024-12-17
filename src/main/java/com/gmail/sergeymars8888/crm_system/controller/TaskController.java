package com.gmail.sergeymars8888.crm_system.controller;


import com.gmail.sergeymars8888.crm_system.dto.TaskDTO;
import com.gmail.sergeymars8888.crm_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    private final TaskService taskService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TaskController(TaskService taskService, SimpMessagingTemplate messagingTemplate) {
        this.taskService = taskService;
        this.messagingTemplate = messagingTemplate;
    }

    private void sendTaskDueDateNotification(TaskDTO taskDTO) {
        messagingTemplate.convertAndSend("/topic/task-due_date", taskDTO.dueDate());
    }

    private void sendTaskStatusUpdateNotification(TaskDTO taskDTO) {
        messagingTemplate.convertAndSend("/topic/task-status", taskDTO.status());
    }

    private void sendDescriptionNotification(TaskDTO taskDTO) {
        messagingTemplate.convertAndSend("/topic/task-description", taskDTO.description());
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {

        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            TaskDTO task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        sendTaskDueDateNotification(taskDTO);
        sendDescriptionNotification(taskDTO);
        sendTaskStatusUpdateNotification(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        try {


            TaskDTO existingTask = taskService.getTaskById(id);

            boolean isDueDateChanged = !existingTask.dueDate().equals(taskDTO.dueDate());
            boolean isStatusChanged = !existingTask.status().equals(taskDTO.status());
            boolean isDescriptionChanged = !existingTask.description().equals(taskDTO.description());

            TaskDTO updatedTask = taskService.updateTask(id, taskDTO);

            if (isDueDateChanged) {
                sendTaskDueDateNotification(updatedTask);
            }

            if (isStatusChanged) {
                sendTaskStatusUpdateNotification(updatedTask);
            }

            if (isDescriptionChanged) {
                sendDescriptionNotification(updatedTask);
            }

            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
