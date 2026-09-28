package com.paycore.backend.services;

import com.paycore.backend.dtos.Requests.CreateCustomerRequest;
import com.paycore.backend.dtos.Responses.CreateCustomerResponse;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request){
        Customer customer = new Customer(
                request.name()
        );

        customerRepository.save(customer);

        CreateCustomerResponse reponse = new CreateCustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );

        return reponse;
    }
}
