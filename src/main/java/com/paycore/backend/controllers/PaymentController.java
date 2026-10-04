package com.paycore.backend.controllers;


import com.paycore.backend.dtos.requests.CreatePaymentRequest;
import com.paycore.backend.dtos.responses.PaymentResponse;
import com.paycore.backend.dtos.responses.ProcessPaymentResponse;
import com.paycore.backend.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest paymentInfosRes){

        PaymentResponse response = paymentService.createPayment(paymentInfosRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<ProcessPaymentResponse> processPayment(@PathVariable UUID id){
        ProcessPaymentResponse response = paymentService.processPayment(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
