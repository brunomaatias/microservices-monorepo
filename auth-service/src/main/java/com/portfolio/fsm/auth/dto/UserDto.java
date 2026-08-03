package com.portfolio.fsm.auth.dto;

import java.util.UUID;

public record UserDto(
        Long id,
        UUID uuidUser,
        String username,
        String name,
        String email,
        String role
) {
}
