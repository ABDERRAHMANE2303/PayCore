package com.paycore.backend.repositories;

import com.paycore.backend.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("""

            select t 
    from Transaction t
    where t.payment.id = :paymentId
    """
    )
    List<Transaction> findByPaymentId(@Param("paymentId") UUID paymentId);
}