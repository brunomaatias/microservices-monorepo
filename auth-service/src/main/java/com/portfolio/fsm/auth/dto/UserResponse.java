package com.portfolio.fsm.auth.dto;

public record UserResponse(String username, String password, String deviceId, String nfc) {
}
