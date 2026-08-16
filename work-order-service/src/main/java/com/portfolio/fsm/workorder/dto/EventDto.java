package com.portfolio.fsm.workorder.dto;

import java.util.Map;
import java.util.UUID;

public record EventDto(
        UUID eventId,
        String eventType,
        String serviceName,
        long timestamp,
        Map<String, Object> payload
) {}
