package com.portfolio.fsm.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CustomerDto(
        UUID id,
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Phone number is required") String phoneNumber,
        String streetAddress,
        String city,
        String state,
        String zipCode,
        String country
) {}
