package com.portfolio.fsm.customer.services;

import com.portfolio.fsm.customer.dto.CustomerDto;
import com.portfolio.fsm.customer.mapper.CustomerMapper;
import com.portfolio.fsm.customer.models.Customer;
import com.portfolio.fsm.customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerDto createCustomer(CustomerDto request) {
        if (customerRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Customer with email " + request.email() + " already exists.");
        }

        Customer customer = customerMapper.toEntity(request);
        
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new com.portfolio.fsm.customer.exceptions.ResourceNotFoundException("Customer not found"));
        return customerMapper.toResponse(customer);
    }

    public CustomerDto updateCustomer(UUID id, CustomerDto request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new com.portfolio.fsm.customer.exceptions.ResourceNotFoundException("Customer not found"));

        customerMapper.updateEntityFromRequest(request, customer);
        Customer updated = customerRepository.save(customer);
        
        return customerMapper.toResponse(updated);
    }
}
