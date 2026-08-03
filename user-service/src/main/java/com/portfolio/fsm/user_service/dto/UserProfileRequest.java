package com.portfolio.fsm.user_service.dto;

import java.util.UUID;

public record UserProfileRequest(
        UUID authUuid,
        String firstName,
        String lastName,
        String phoneNumber,
        String avatarUrl,
        String addressLine,
        String city,
        String country
) {
}
