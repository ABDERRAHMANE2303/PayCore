package com.paycore.backend.controllers;


import com.paycore.backend.dtos.requests.CreatePaymentRequest;
import com.paycore.backend.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request){

        PaymentResponse response = paymentService.createAndProcessPayment(paymentDetails.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
