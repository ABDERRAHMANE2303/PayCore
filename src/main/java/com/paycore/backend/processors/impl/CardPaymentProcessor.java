package com.paycore.backend.processors.impl;

import com.paycore.backend.processors.ProcessingResult;
import com.paycore.backend.entities.Payment;
import com.paycore.backend.enums.PaymentStatus;
import com.paycore.backend.processors.PaymentProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class CardPaymentProcessor implements PaymentProcessor {

    private BigDecimal maximumAmount = new BigDecimal("20000");
    private BigDecimal fee = new BigDecimal("0.02");

    public CardPaymentProcessor() {}

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
        ProcessingResult result = new ProcessingResult(
                payment.getId(),
                PaymentStatus.SUCCESS,
                feeAmount
        );
        return result;
    }

}
