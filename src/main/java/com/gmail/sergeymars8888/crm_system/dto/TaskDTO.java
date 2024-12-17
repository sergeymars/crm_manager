package com.gmail.sergeymars8888.crm_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.sergeymars8888.crm_system.model.TaskStatus;

import java.time.LocalDateTime;

public record TaskDTO(
        Long id,
        String description,
        TaskStatus status,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        LocalDateTime dueDate,
        Long contactId

) {
}
