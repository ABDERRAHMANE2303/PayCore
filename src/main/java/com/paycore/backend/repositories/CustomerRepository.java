package com.paycore.backend.repositories;

import com.paycore.backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    public List<Customer> findByStatus(Customer.CustomerStatus status);
}
