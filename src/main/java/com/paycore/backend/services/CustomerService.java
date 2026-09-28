package com.paycore.backend.services;

import com.paycore.backend.dtos.Requests.CreateCustomerRequest;
import com.paycore.backend.dtos.Responses.CustomerInfosResponse;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.exceptions.custom.CustomerNotFoundException;
import com.paycore.backend.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerInfosResponse createCustomer(CreateCustomerRequest request){
        Customer customer = new Customer(
                request.name()
        );

        customerRepository.save(customer);

        CustomerInfosResponse reponse = new CustomerInfosResponse(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );

        return reponse;
    }

    @Transactional
    public CustomerInfosResponse getCustomerById(UUID id){
        Customer customer = customerRepository.findById(id).
                orElseThrow(
                        () -> new CustomerNotFoundException("Customer " + id + " was not found")
                );
        CustomerInfosResponse reponse = new CustomerInfosResponse(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );
        return reponse;
    }
}
