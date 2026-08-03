package com.portfolio.fsm.auth.dto;

public record LoginRequest(
        String username,
        String password,
        String deviceId,
        String nfc
) {
}
