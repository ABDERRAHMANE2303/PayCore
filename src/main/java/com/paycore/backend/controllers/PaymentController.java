package com.paycore.backend.controllers;


import com.paycore.backend.dtos.Requests.CreatePaymentReq;
import com.paycore.backend.dtos.Responses.PaymentInfosRes;
import com.paycore.backend.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentInfosRes> createPayment(
            @Valid @RequestBody CreatePaymentReq paymentInfosRes){

        PaymentInfosRes response = paymentService.createPayment(paymentInfosRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
