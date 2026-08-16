package com.portfolio.fsm.customer.services;

import com.portfolio.fsm.customer.dto.CustomerDto;
import com.portfolio.fsm.customer.mapper.CustomerMapper;
import com.portfolio.fsm.customer.models.Customer;
import com.portfolio.fsm.customer.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDto requestDto;
    private Customer entity;
    private CustomerDto responseDto;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();

        requestDto = new CustomerDto(null, "John", "Doe", "john@example.com", "555-0100", "123 St", "Austin", "TX", "78701", "USA");

        entity = new Customer();
        entity.setId(customerId);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("john@example.com");

        responseDto = new CustomerDto(customerId, "John", "Doe", "john@example.com", "555-0100", "123 St", "Austin", "TX", "78701", "USA");
    }

    @Test
    void createCustomer_Success() {
        when(customerRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(customerMapper.toEntity(requestDto)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(entity);
        when(customerMapper.toResponse(entity)).thenReturn(responseDto);

        CustomerDto result = customerService.createCustomer(requestDto);

        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("John", result.firstName());

        verify(customerRepository, times(1)).findByEmail(requestDto.email());
        verify(customerRepository, times(1)).save(entity);
    }

    @Test
    void createCustomer_EmailAlreadyExists_ThrowsException() {
        when(customerRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(entity));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.createCustomer(requestDto);
        });

        assertTrue(exception.getMessage().contains("already exists"));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void getCustomerById_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(entity));
        when(customerMapper.toResponse(entity)).thenReturn(responseDto);

        CustomerDto result = customerService.getCustomerById(customerId);

        assertNotNull(result);
        assertEquals("John", result.firstName());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void getCustomerById_NotFound_ThrowsException() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(customerId);
        });
    }

    @Test
    void getAllCustomers_Success() {
        when(customerRepository.findAll()).thenReturn(List.of(entity));
        when(customerMapper.toResponse(entity)).thenReturn(responseDto);

        List<CustomerDto> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        verify(customerRepository, times(1)).findAll();
    }
}
