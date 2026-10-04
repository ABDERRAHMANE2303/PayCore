package com.paycore.backend.utilities;

import com.paycore.backend.dtos.Responses.CustomerInfosRes;
import com.paycore.backend.dtos.Responses.MerchantInfosRes;
import com.paycore.backend.dtos.Responses.PaymentInfosRes;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.entities.Payment;
import org.springframework.stereotype.Component;


@Component
public class EntityDtoMapper {

    public CustomerInfosRes customerEntityDtoMapper(Customer customer){
        CustomerInfosRes reponse = new CustomerInfosRes(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );

        return reponse;
    }

    public MerchantInfosRes merchantEntityDtoMapper(Merchant merchant){
        MerchantInfosRes response = new MerchantInfosRes(
                merchant.getId(),
                merchant.getName(),
                merchant.getCategory(),
                merchant.getStatus(),
                merchant.getCreatedAt()
        );
        return response;
    }

    public PaymentInfosRes paymentEntityDtoMapper(Payment payment){

        CustomerInfosRes customerInfos =  customerEntityDtoMapper(payment.getCustomer());
        MerchantInfosRes merchantInfos =  merchantEntityDtoMapper(payment.getMerchant());

        PaymentInfosRes response = new PaymentInfosRes(
                payment.getId(),
                customerInfos,
                merchantInfos,
                payment.getAmount(),
                payment.getCurrency(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getProcessedAt()
        );
        return response;
    }


}
