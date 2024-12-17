package com.gmail.sergeymars8888.crm_system.mapper;

import com.gmail.sergeymars8888.crm_system.dto.TaskDTO;
import com.gmail.sergeymars8888.crm_system.model.Task;

public interface TaskMapper {

    TaskDTO toDto(Task task);
    Task toEntity(TaskDTO taskDTO);
}
