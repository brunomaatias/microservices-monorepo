package com.portfolio.fsm.auth.dto;

import java.util.Map;
import java.util.UUID;

public record EventDto(
        String eventType,
        UUID userId,
        Map<String, Object> details,
        long timestamp
) {
    public EventDto(String eventType, UUID userId, Map<String, Object> details) {
        this(eventType, userId, details, System.currentTimeMillis());
    }
}
