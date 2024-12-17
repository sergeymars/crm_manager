package com.gmail.sergeymars8888.crm_system.mapper.impl;

import com.gmail.sergeymars8888.crm_system.dto.TaskDTO;
import com.gmail.sergeymars8888.crm_system.mapper.TaskMapper;
import com.gmail.sergeymars8888.crm_system.model.Task;
import org.springframework.stereotype.Component;


@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDTO toDto(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDTO(
                task.getId(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getContact() != null ? task.getContact().getId() : null
        );
    }

    @Override
    public Task toEntity(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }

        Task task = new Task();
        task.setId(taskDTO.id());
        task.setDescription(taskDTO.description());
        task.setStatus(taskDTO.status());
        task.setDueDate(taskDTO.dueDate());
        return task;
    }
}
