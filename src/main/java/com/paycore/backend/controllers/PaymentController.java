package com.paycore.backend.controllers;


import com.paycore.backend.dtos.requests.CreatePaymentRequest;
import com.paycore.backend.dtos.requests.CreateRefundRequest;
import com.paycore.backend.dtos.responses.PaymentDetailsResponse;
import com.paycore.backend.dtos.responses.PaymentResponse;
import com.paycore.backend.dtos.responses.TransactionDetailsResponse;
import com.paycore.backend.enums.PaymentMethod;
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
    public ResponseEntity<PaymentDetailsResponse> getPayment(@PathVariable UUID id){
        PaymentDetailsResponse response = paymentService.getPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDetailsResponse>> listPayments(
            @RequestParam(required = false)
            UUID customerId,
            @RequestParam(required = false)
            UUID merchantId,
            @RequestParam(required = false)
            PaymentStatus status,
            @RequestParam(required = false)
            PaymentMethod method,
            @RequestParam(required = false)
            BigDecimal minAmount,
            @RequestParam(required = false)
            BigDecimal  maxAmount,
            @RequestParam(required = false , defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false , defaultValue = "desc") String direction

    ){
        List<PaymentDetailsResponse> response = paymentService.getPayments(
                customerId,
                merchantId,
                status,
                method,
                minAmount,
                maxAmount,
                sortBy,
                direction
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDetailsResponse>> listTransactions(
            @PathVariable UUID id
    ){
        List<TransactionDetailsResponse> response = paymentService.getTransactions(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
