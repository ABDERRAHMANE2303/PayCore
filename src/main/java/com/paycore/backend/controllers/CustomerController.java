package com.paycore.backend.controllers;


import com.paycore.backend.dtos.Requests.ChangeCustomerStatusRequest;
import com.paycore.backend.dtos.Requests.CreateCustomerRequest;
import com.paycore.backend.dtos.Responses.CustomerInfosResponse;
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
    public ResponseEntity<CustomerInfosResponse> createCustomer(@Valid
                                                                 @RequestBody CreateCustomerRequest request){
        CustomerInfosResponse response = customerService.createCustomer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInfosResponse> getCustomer(@PathVariable UUID id){
        CustomerInfosResponse response = customerService.getCustomerById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerInfosResponse>> getAllCustomers(
            @RequestParam(required = false)
            CustomerStatus status){
        List<CustomerInfosResponse> response = customerService.getAllCustomer(status);
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
