package com.paycore.backend.entities;


import com.paycore.backend.enums.Currency;
import com.paycore.backend.enums.PaymentMethod;
import com.paycore.backend.enums.PaymentStatus;
import com.paycore.backend.exceptions.custom.InvalidPaymentStatusException;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
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

    public void markSuccesful(){
        if (status != PaymentStatus.PROCESSING){
            throw new InvalidPaymentStatusException(
                    "Only a processing payment can succeed");
        }
        this.status = PaymentStatus.SUCCESS;
        this.processedAt = LocalDateTime.now();

    }

    public void markFailed(){
        if (status != PaymentStatus.PROCESSING){
            throw new InvalidPaymentStatusException(
                    "Only a processing payment can fail");
        }
        this.status = PaymentStatus.FAILED;
        this.processedAt = LocalDateTime.now();
    }


    public void startProcessing(){
        if (status != PaymentStatus.PENDING){
            throw new InvalidPaymentStatusException(
                    "Only a pending payment can start processing"
            );
        }
        this.status = PaymentStatus.PROCESSING;
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
