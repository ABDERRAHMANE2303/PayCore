package com.paycore.backend.repositories;

import com.paycore.backend.dtos.Requests.CreateCustomerRequest;
import com.paycore.backend.dtos.Responses.CreateCustomerResponse;
import com.paycore.backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
