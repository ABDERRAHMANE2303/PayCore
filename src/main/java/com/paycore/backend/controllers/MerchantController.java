package com.paycore.backend.controllers;


import com.paycore.backend.dtos.requests.ChangeMerchantStatusRequest;
import com.paycore.backend.dtos.requests.CreateMerchantRequest;
import com.paycore.backend.dtos.responses.MerchantResponse;
import com.paycore.backend.enums.MerchantCategory;
import com.paycore.backend.enums.MerchantStatus;
import com.paycore.backend.services.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping
    public ResponseEntity<MerchantResponse> createMerchant(
            @Valid @RequestBody CreateMerchantRequest request){
        MerchantResponse response = merchantService.createMerchant(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> getMerchantById(@PathVariable UUID id){
        MerchantResponse response = merchantService.getMerchant(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MerchantResponse>> getAllMerchants(
            @RequestParam(required = false)
            MerchantStatus status,
            @RequestParam(required = false)
            MerchantCategory category
    ){
        List<MerchantResponse> resp = merchantService.listMerchants(status, category);
        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String>  updateMerchantStatus(
        @PathVariable UUID id,
        @Valid @RequestBody ChangeMerchantStatusRequest request
    ){
        merchantService.changeMerchantStatus(id,request);
        return ResponseEntity.ok("Status changed successfully");
    }
}
