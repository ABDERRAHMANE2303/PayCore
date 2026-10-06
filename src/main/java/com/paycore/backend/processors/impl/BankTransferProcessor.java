package com.paycore.backend.processors.impl;

import com.paycore.backend.processors.ProcessingResult;
import com.paycore.backend.entities.Payment;
import com.paycore.backend.enums.PaymentStatus;
import com.paycore.backend.processors.PaymentProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class BankTransferProcessor implements PaymentProcessor {

    final private BigDecimal maximumAmount = new BigDecimal("100000");
    final private BigDecimal fee =  new BigDecimal("0.01");

    public BankTransferProcessor() {}

    @Override
    public ProcessingResult processPayment(Payment payment){
        if (payment.getAmount().compareTo(maximumAmount) > 0 ) {
            return new ProcessingResult(
                    payment.getId(),
                    PaymentStatus.FAILED,
                    new BigDecimal("0")
            );
        }

        BigDecimal feeAmount = payment.getAmount().multiply(this.fee);
        return new ProcessingResult(
                payment.getId(),
                PaymentStatus.SUCCESS,
                feeAmount
        );
    }

}
