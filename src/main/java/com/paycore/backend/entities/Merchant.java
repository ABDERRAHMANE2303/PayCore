package com.paycore.backend.entities;


import com.paycore.backend.enums.MerchantCategory;
import com.paycore.backend.enums.MerchantStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "merchant")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MerchantCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MerchantStatus status =  MerchantStatus.ACTIVE;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }

    public Merchant(String name , MerchantCategory category) {
        this.name = name;
        this.category = category;
    }

    protected Merchant() {}

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public MerchantCategory getCategory() {
        return category;
    }
    public  MerchantStatus getStatus() {
        return status;
    }
    public void setStatus(MerchantStatus status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
