package com.paycore.backend.repositories;

import com.paycore.backend.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByPayment_Id(UUID paymentId);
}