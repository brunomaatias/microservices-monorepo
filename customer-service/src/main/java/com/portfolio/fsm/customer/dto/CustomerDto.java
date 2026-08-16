package com.portfolio.fsm.customer.dto;

import java.util.UUID;

public record CustomerDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String streetAddress,
        String city,
        String state,
        String zipCode,
        String country
) {}
