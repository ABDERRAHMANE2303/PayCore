package com.paycore.backend.services;


import com.paycore.backend.dtos.requests.ChangeMerchantStatusRequest;
import com.paycore.backend.dtos.requests.CreateMerchantRequest;
import com.paycore.backend.dtos.responses.MerchantDetailResponse;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.enums.MerchantCategory;
import com.paycore.backend.enums.MerchantStatus;
import com.paycore.backend.exceptions.custom.ResourceNotFoundException;
import com.paycore.backend.repositories.MerchantRepository;
import com.paycore.backend.utilities.EntityDtoMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final EntityDtoMapper entityDtoMapper;

    public MerchantService( MerchantRepository merchantRepository,
                            EntityDtoMapper entityDtoMapper) {
        this.merchantRepository = merchantRepository;
        this.entityDtoMapper = entityDtoMapper;
    }

    @Transactional
    public MerchantDetailResponse createMerchant(CreateMerchantRequest request) {
        Merchant merchant = new Merchant(
                request.name(),
                request.category()
        );

        merchantRepository.save(merchant);

        MerchantDetailResponse response = entityDtoMapper.merchantEntityDtoMapper(merchant);

        return  response;
    }

    @Transactional
    public MerchantDetailResponse getMerchant(UUID id) {
        Merchant merchant = merchantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Merchant" + id+ "Not found"));
        MerchantDetailResponse response = entityDtoMapper.merchantEntityDtoMapper(merchant);
        return response;
    }

    @Transactional
    public List<MerchantDetailResponse> listMerchants(
            MerchantStatus status,
            MerchantCategory category) {

        List<MerchantDetailResponse> response = new ArrayList<>();
        List<Merchant> merchants = new ArrayList<>();
        if (status == null && category == null) {
            merchants = merchantRepository.findAll();
        } else if (status == null) {
            merchants = merchantRepository.findByCategory(category);
        } else if (category == null) {
            merchants = merchantRepository.findByStatus(status);
        } else {
            merchants = merchantRepository.findByStatusAndCategory(status, category);
        }

        for (Merchant merchant :  merchants) {
            MerchantDetailResponse merchantInfos = entityDtoMapper.merchantEntityDtoMapper(merchant);
            response.add(merchantInfos);
        }
        return response;
    }

    @Transactional
    public  void changeMerchantStatus(UUID id, ChangeMerchantStatusRequest request){
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant" + id+ "Not found"));
        merchant.setStatus(request.status());
        merchantRepository.save(merchant);

    }

}
