package com.paycore.backend.controllers;


import com.paycore.backend.dtos.requests.ChangeCustomerStatusRequest;
import com.paycore.backend.dtos.requests.CreateCustomerRequest;
import com.paycore.backend.dtos.responses.CustomerDetailsResponse;
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
    public ResponseEntity<CustomerDetailsResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request){
        CustomerDetailsResponse response = customerService.createCustomer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsResponse> getCustomer(@PathVariable UUID id){
        CustomerDetailsResponse response = customerService.getCustomerById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerDetailsResponse>> getAllCustomers(
            @RequestParam(required = false)
            CustomerStatus status){
        List<CustomerDetailsResponse> response = customerService.getAllCustomer(status);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeCustomerStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeCustomerStatusRequest request){
        customerService.changeCustomerStatus(id, request);
        return ResponseEntity.ok("Status updated successfully");
    }
}
