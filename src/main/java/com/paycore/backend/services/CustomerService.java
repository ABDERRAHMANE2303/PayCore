package com.paycore.backend.services;

import com.paycore.backend.dtos.Requests.ChangeCustomerStatusReq;
import com.paycore.backend.dtos.Requests.CreateCustomerReq;
import com.paycore.backend.dtos.Responses.CustomerInfosRes;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.enums.CustomerStatus;
import com.paycore.backend.exceptions.custom.CustomerNotFoundException;
import com.paycore.backend.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerInfosRes createCustomer(CreateCustomerReq request){

        Customer customer = new Customer(
                request.name()
        );

        customerRepository.save(customer);

        CustomerInfosRes reponse = new CustomerInfosRes(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );

        return reponse;
    }

    @Transactional
    public CustomerInfosRes getCustomerById(UUID id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer " + id + " was not found")
                );
        CustomerInfosRes reponse = new CustomerInfosRes(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );
        return reponse;
    }

    @Transactional
    public List<CustomerInfosRes> getAllCustomer(CustomerStatus status){

        List<Customer> customers = status == null
                ?customerRepository.findAll()
                :customerRepository.findByStatus(status);

        List<CustomerInfosRes> response = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerInfosRes customerInfos = new CustomerInfosRes(
                    customer.getId(),
                    customer.getName(),
                    customer.getCreatedAt(),
                    customer.getStatus()
            );
            response.add(customerInfos);
        }

        return response;
    }

    @Transactional
    public void changeCustomerStatus(UUID id, ChangeCustomerStatusReq request){
        Customer customer = customerRepository.findById(id)
                        .orElseThrow(
                                () -> new CustomerNotFoundException("customer" + id + "Not found")
                        );
        customer.setStatus(request.status());
        customerRepository.save(customer);
    }
}

