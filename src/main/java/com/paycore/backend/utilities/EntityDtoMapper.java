package com.paycore.backend.utilities;

import com.paycore.backend.dtos.responses.CustomerResponse;
import com.paycore.backend.dtos.responses.MerchantResponse;
import com.paycore.backend.dtos.responses.PaymentResponse;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.entities.Payment;
import org.springframework.stereotype.Component;


@Component
public class EntityDtoMapper {

    public CustomerResponse customerEntityDtoMapper(Customer customer){
        CustomerResponse reponse = new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );

        return reponse;
    }

    public MerchantResponse merchantEntityDtoMapper(Merchant merchant){
        MerchantResponse response = new MerchantResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getCategory(),
                merchant.getStatus(),
                merchant.getCreatedAt()
        );
        return response;
    }

    public PaymentResponse paymentEntityDtoMapper(Payment payment){

        CustomerResponse customerInfos =  customerEntityDtoMapper(payment.getCustomer());
        MerchantResponse merchantInfos =  merchantEntityDtoMapper(payment.getMerchant());

        PaymentResponse response = new PaymentResponse(
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
