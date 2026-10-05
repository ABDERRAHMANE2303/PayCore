package com.paycore.backend.repositories;

import com.paycore.backend.entities.Payment;
import com.paycore.backend.enums.PaymentMethod;
import com.paycore.backend.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    public List<Payment> findByStatus(PaymentStatus status);

    @Query("""
        select p
        from Payment p
        where p.merchant.id = :merchantId        
    """)
    public List<Payment> findByMerchanrId(@Param("merchantId") UUID merchantId);

    @Query("""
        select p
        from Payment p
        where p.customer.id = :customerId
    """)
    public List<Payment> findByCustomerId(@Param("customerId") UUID customerId);


    @Query("""
        select p
        from Payment p
        where p.amount >= :minAmount
    """)
    public List<Payment> findByMinAmount(@Param("minAmount") BigDecimal minAmount);

    @Query("""
        select p
        from Payment p
        where p.amount <= :maxAmount
    """)
    public List<Payment> findByMaxAmount(@Param("maxAmount") BigDecimal maxAmount);

    @Query("""
    select p
    from Payment p 
    where p.method = :methof
    """)
    public List<Payment> findByMethod(@Param("method")PaymentMethod method);


}
