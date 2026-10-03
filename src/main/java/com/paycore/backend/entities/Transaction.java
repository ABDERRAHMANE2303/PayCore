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

    @Column
    private TransactionType type;

    @Column
    private BigDecimal amount;

    @Column
    BigDecimal fee;

    @Column
    TransactionStatus status;

    @Column
    private LocalDateTime createdAt;



}
