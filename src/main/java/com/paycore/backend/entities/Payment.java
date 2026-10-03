package com.paycore.backend.entities;


import com.paycore.backend.enums.Currency;
import com.paycore.backend.enums.PaymentMethod;
import com.paycore.backend.enums.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name  = "payment")
public class Payment {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn ( name = "customer_id" , nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn ( name = "merchant_id" , nullable = false)
    private Merchant merchant;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column
    private LocalDateTime processedAt;

    @Column
    private LocalDateTime refundedAt;

    @PrePersist
    void Oncreate() {
        createdAt = LocalDateTime.now();
        status = PaymentStatus.PENDING;
    }

    public Payment() {}

    public Payment(Customer customer,
                   Merchant merchant,
                   BigDecimal amount,
                   Currency currency,
                   PaymentMethod method) {
        this.customer = customer;
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.method = method;
    }

    public UUID getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public LocalDateTime getRefundedAt() {
        return refundedAt;
    }
}
