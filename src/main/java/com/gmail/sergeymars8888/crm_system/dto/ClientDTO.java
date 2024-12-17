package com.gmail.sergeymars8888.crm_system.dto;

import java.util.List;

public record ClientDTO(
        Long id,
        String name,
        String industry,
        String address,
        List<Long> contactIds
) {
}
