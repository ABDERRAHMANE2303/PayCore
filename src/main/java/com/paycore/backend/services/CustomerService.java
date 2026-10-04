package com.paycore.backend.services;

import com.paycore.backend.dtos.requests.ChangeCustomerStatusRequest;
import com.paycore.backend.dtos.requests.CreateCustomerRequest;
import com.paycore.backend.dtos.responses.CustomerResponse;
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
    public CustomerResponse createCustomer(CreateCustomerRequest request){

        Customer customer = new Customer(
                request.name()
        );

        customerRepository.save(customer);

        CustomerResponse reponse = entityDtoMapper.customerEntityDtoMapper(customer);

        return reponse;
    }

    @Transactional
    public CustomerResponse getCustomerById(UUID id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer " + id + " was not found")
                );
        CustomerResponse reponse = entityDtoMapper.customerEntityDtoMapper(customer);
        return reponse;
    }

    @Transactional
    public List<CustomerResponse> getAllCustomer(CustomerStatus status){

        List<Customer> customers = status == null
                ?customerRepository.findAll()
                :customerRepository.findByStatus(status);

        List<CustomerResponse> response = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerResponse customerInfos = entityDtoMapper.customerEntityDtoMapper(customer);
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

