package com.portfolio.fsm.workorder.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkOrderDto(
        UUID id,
        String title,
        String description,
        String status,
        UUID customerId,
        UUID technicianId,
        LocalDateTime scheduledDate
) {}
