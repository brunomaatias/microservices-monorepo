package com.portfolio.fsm.auth.dto;

import com.portfolio.fsm.auth.models.User;

public record LoginResponse(String token, User user) {
}
