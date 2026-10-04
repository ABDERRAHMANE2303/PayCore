package com.paycore.backend.entities;


import com.paycore.backend.enums.TransactionStatus;
import com.paycore.backend.enums.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn ( name = "payment_id" , nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal fee;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Transaction(Payment payment,
                       TransactionType type,
                       BigDecimal amount,
                       BigDecimal fee,
                       TransactionStatus status) {
        this.payment = payment;
        this.type = type;
        this.amount = amount;
        this.fee = fee;
        this.status = status;
    }

    protected Transaction() {}

    public UUID getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
