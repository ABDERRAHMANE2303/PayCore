package com.paycore.backend.entities;

import com.paycore.backend.enums.CustomerStatus;
import jakarta.persistence.*;
import jdk.jfr.Enabled;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status =  CustomerStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    void Oncreate(){
        createdAt = LocalDateTime.now();
    }

    public Customer (String name) {
        this.name = name;
    }

    protected Customer() {

    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
