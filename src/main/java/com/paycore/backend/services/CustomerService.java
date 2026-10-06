package com.paycore.backend.services;

import com.paycore.backend.dtos.requests.ChangeCustomerStatusRequest;
import com.paycore.backend.dtos.requests.CreateCustomerRequest;
import com.paycore.backend.dtos.responses.CustomerDetailsResponse;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.enums.CustomerStatus;
import com.paycore.backend.exceptions.custom.ResourceNotFoundException;
import com.paycore.backend.repositories.CustomerRepository;
import com.paycore.backend.utilities.EntityDtoMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final EntityDtoMapper entityDtoMapper;


    public CustomerService(CustomerRepository customerRepository,
                           EntityDtoMapper entityDtoMapper) {
        this.customerRepository = customerRepository;
        this.entityDtoMapper = entityDtoMapper;
    }

    @Transactional
    public CustomerDetailsResponse createCustomer(CreateCustomerRequest request){

        Customer customer = new Customer(
                request.name()
        );

        customerRepository.save(customer);

        return entityDtoMapper.customerEntityDtoMapper(customer);

    }

    @Transactional
    public CustomerDetailsResponse getCustomerById(UUID id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer " + id + " was not found")
                );
        return entityDtoMapper.customerEntityDtoMapper(customer);
    }

    @Transactional
    public List<CustomerDetailsResponse> getAllCustomer(CustomerStatus status){

        List<Customer> customers = status == null
                ?customerRepository.findAll()
                :customerRepository.findByStatus(status);

        List<CustomerDetailsResponse> response = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDetailsResponse customerInfos = entityDtoMapper.customerEntityDtoMapper(customer);
            response.add(customerInfos);
        }

        return response;
    }

    @Transactional
    public void changeCustomerStatus(UUID id, ChangeCustomerStatusRequest request){
        Customer customer = customerRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("customer" + id + "Not found")
                        );
        customer.setStatus(request.status());
        customerRepository.save(customer);
    }
}

