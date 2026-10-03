package com.paycore.backend.processors.impl;

import com.paycore.backend.dtos.Responses.ProcessingResult;
import com.paycore.backend.entities.Payment;
import com.paycore.backend.enums.PaymentStatus;
import com.paycore.backend.processors.PaymentProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletPaymentProcessor implements PaymentProcessor {

    private BigDecimal maximumAmount = new BigDecimal("5000");
    private BigDecimal fee = new BigDecimal("0.005");

    public WalletPaymentProcessor() {}

    @Override
    public ProcessingResult processPayment(Payment payment){

        if (payment.getAmount().compareTo(maximumAmount) > 0 ) {
            payment.setStatus(PaymentStatus.FAILED);

            return new ProcessingResult(
                    payment.getId(),
                    payment.getStatus(),
                    new BigDecimal("0")
            );
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        BigDecimal feeAmount = payment.getAmount().multiply(this.fee);

        ProcessingResult result = new ProcessingResult(
                payment.getId(),
                payment.getStatus(),
                feeAmount
        );

        return result;
    }
}
