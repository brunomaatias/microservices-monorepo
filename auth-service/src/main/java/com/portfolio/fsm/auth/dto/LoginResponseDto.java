package com.portfolio.fsm.auth.dto;

public record LoginResponseDto(
        String token,
        UserDto user
) {
}
