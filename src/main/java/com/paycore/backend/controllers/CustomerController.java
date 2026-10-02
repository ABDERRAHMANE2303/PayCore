package com.paycore.backend.controllers;


import com.paycore.backend.dtos.Requests.ChangeCustomerStatusReq;
import com.paycore.backend.dtos.Requests.CreateCustomerReq;
import com.paycore.backend.dtos.Responses.CustomerInfosRes;
import com.paycore.backend.enums.CustomerStatus;
import com.paycore.backend.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    public ResponseEntity<CustomerInfosRes> createCustomer(@Valid @RequestBody CreateCustomerReq request){
        CustomerInfosRes response = customerService.createCustomer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInfosRes> getCustomer(@PathVariable UUID id){
        CustomerInfosRes response = customerService.getCustomerById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerInfosRes>> getAllCustomers(
            @RequestParam(required = false)
            CustomerStatus status){
        List<CustomerInfosRes> response = customerService.getAllCustomer(status);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeCustomerStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeCustomerStatusReq request){
        customerService.changeCustomerStatus(id, request);
        return ResponseEntity.ok("Status updated successfully");
    }
}
