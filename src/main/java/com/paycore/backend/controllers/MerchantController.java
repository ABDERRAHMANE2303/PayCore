package com.paycore.backend.controllers;


import com.paycore.backend.dtos.Requests.ChangeMerchantStatusReq;
import com.paycore.backend.dtos.Requests.CreateMerchantReq;
import com.paycore.backend.dtos.Responses.MerchantInfosRes;
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
    public ResponseEntity<MerchantInfosRes> createMerchant(
            @Valid @RequestBody CreateMerchantReq request){
        MerchantInfosRes response = merchantService.createMerchant(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantInfosRes> getMerchantById(@PathVariable UUID id){
        MerchantInfosRes response = merchantService.getMerchant(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MerchantInfosRes>> getAllMerchants(
            @RequestParam(required = false)
            MerchantStatus status,
            @RequestParam(required = false)
            MerchantCategory category
    ){
        List<MerchantInfosRes> resp = merchantService.listMerchants(status, category);
        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String>  updateMerchantStatus(
        @PathVariable UUID id,
        @Valid @RequestBody ChangeMerchantStatusReq request
    ){
        merchantService.changeMerchantStatus(id,request);
        return ResponseEntity.ok("Status changed successfully");
    }
}
