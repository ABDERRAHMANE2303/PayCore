package com.paycore.backend.controllers;


import com.paycore.backend.dtos.requests.CreatePaymentRequest;
import com.paycore.backend.dtos.requests.CreateRefundRequest;
import com.paycore.backend.dtos.responses.PaymentDetails;
import com.paycore.backend.dtos.responses.PaymentResponse;
import com.paycore.backend.enums.PaymentStatus;
import com.paycore.backend.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
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
            @Valid @RequestBody CreatePaymentRequest request){

        PaymentResponse response = paymentService.createAndProcessPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @Valid @RequestBody CreateRefundRequest request,
            @PathVariable UUID id
    ){
        PaymentResponse response = paymentService.createRefund(request,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetails> getPayment(@PathVariable UUID id){
        PaymentDetails response = paymentService.getPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDetails>> getPayments(
            @RequestParam(required = false)
            UUID customerId,
            @RequestParam(required = false)
            UUID merchanId,
            @RequestParam(required = false)
            PaymentStatus status,
            @RequestParam(required = false)
            BigDecimal minAmount,
            @RequestParam(required = false)
            BigDecimal  maxAmount
    ){

    }


}
