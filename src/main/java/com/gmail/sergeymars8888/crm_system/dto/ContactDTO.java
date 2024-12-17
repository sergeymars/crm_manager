package com.gmail.sergeymars8888.crm_system.dto;

import java.util.List;

public record ContactDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,

        Long clientId,
        List<Long> tasks
) {
}
