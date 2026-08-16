package com.portfolio.fsm.customer.mapper;

import com.portfolio.fsm.customer.dto.CustomerDto;
import com.portfolio.fsm.customer.models.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto toResponse(Customer entity) {
        if (entity == null) {
            return null;
        }

        return new CustomerDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getStreetAddress(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode(),
                entity.getCountry()
        );
    }

    public Customer toEntity(CustomerDto request) {
        if (request == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setStreetAddress(request.streetAddress());
        customer.setCity(request.city());
        customer.setState(request.state());
        customer.setZipCode(request.zipCode());
        customer.setCountry(request.country());

        return customer;
    }

    public void updateEntityFromRequest(CustomerDto request, Customer entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.firstName() != null) {
            entity.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            entity.setLastName(request.lastName());
        }
        if (request.email() != null) {
            entity.setEmail(request.email());
        }
        if (request.phoneNumber() != null) {
            entity.setPhoneNumber(request.phoneNumber());
        }
        if (request.streetAddress() != null) {
            entity.setStreetAddress(request.streetAddress());
        }
        if (request.city() != null) {
            entity.setCity(request.city());
        }
        if (request.state() != null) {
            entity.setState(request.state());
        }
        if (request.zipCode() != null) {
            entity.setZipCode(request.zipCode());
        }
        if (request.country() != null) {
            entity.setCountry(request.country());
        }
    }
}
