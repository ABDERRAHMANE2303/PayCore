package com.paycore.backend.processors;

import com.paycore.backend.entities.Payment;

public interface PaymentProcessor {

    ProcessingResult processPayment(Payment payment);

}
