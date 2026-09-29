package com.paycore.backend.services;


import com.paycore.backend.dtos.Requests.ChangeMerchantStatusReq;
import com.paycore.backend.dtos.Requests.CreateMerchantReq;
import com.paycore.backend.dtos.Responses.MerchantInfosRes;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.enums.MerchantCategory;
import com.paycore.backend.enums.MerchantStatus;
import com.paycore.backend.exceptions.custom.MerchantNotFoundException;
import com.paycore.backend.repositories.MerchantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService( MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Transactional
    public MerchantInfosRes createMerchant(CreateMerchantReq request) {
        Merchant merchant = new Merchant(
                request.name(),
                request.category()
        );

        merchantRepository.save(merchant);

        MerchantInfosRes response = new MerchantInfosRes(
                merchant.getId(),
                merchant.getName(),
                merchant.getCategory(),
                merchant.getStatus(),
                merchant.getCreatedAt()
        );

        return  response;
    }

    @Transactional
    public MerchantInfosRes getMerchant(UUID id) {
        Merchant merchant = merchantRepository.findById(id).
                orElseThrow(() -> new MerchantNotFoundException("Merchant" + id+ "Not found"));
        MerchantInfosRes response = new MerchantInfosRes(
                merchant.getId(),
                merchant.getName(),
                merchant.getCategory(),
                merchant.getStatus(),
                merchant.getCreatedAt()
        );
        return response;
    }

    @Transactional
    public List<MerchantInfosRes> listMerchants(
            MerchantStatus status,
            MerchantCategory category) {

        List<MerchantInfosRes> response = new ArrayList<>();
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
            MerchantInfosRes merchantInfos = new MerchantInfosRes(
                    merchant.getId(),
                    merchant.getName(),
                    merchant.getCategory(),
                    merchant.getStatus(),
                    merchant.getCreatedAt()
            );
            response.add(merchantInfos);
        }
        return response;
    }

    @Transactional
    public  void changeMerchantStatus(UUID id, ChangeMerchantStatusReq request){
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant" + id+ "Not found"));
        merchant.setStatus(request.status());
        merchantRepository.save(merchant);

    }

}
