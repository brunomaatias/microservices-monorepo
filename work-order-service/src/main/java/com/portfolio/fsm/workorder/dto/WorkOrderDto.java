package com.portfolio.fsm.workorder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record WorkOrderDto(
        UUID id,
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Description is required") String description,
        @NotBlank(message = "Status is required") String status,
        @NotNull(message = "Customer ID is required") UUID customerId,
        UUID technicianId,
        LocalDateTime scheduledDate
) {}
