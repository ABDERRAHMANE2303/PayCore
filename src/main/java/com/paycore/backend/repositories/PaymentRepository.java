package com.paycore.backend.repositories;

import com.paycore.backend.entities.Payment;
import com.paycore.backend.enums.PaymentMethod;
import com.paycore.backend.enums.PaymentStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {


    @Query("""
    select p
    from Payment p 
    where (:customerId is null or p.customer.id = :customerId)
        and (:merchantId is null or p.merchant.id = :merchantId)
        and (:method is null or p.method = :method)
        and (:status is null or p.status = :status)
        and (:minAmount is null or p.amount >= :minAmount)
        and (:maxAmount is null or p.amount <= :maxAmount)
    """)
    public List<Payment> search(@Param("customerId") UUID customerId,
                                @Param("merchantId") UUID merchantId,
                                @Param("status") PaymentStatus status,
                                @Param("method") PaymentMethod method,
                                @Param("minAmount")  BigDecimal minAmount,
                                @Param("maxAmount") BigDecimal maxAmount,
                                Sort sort

    );


}
