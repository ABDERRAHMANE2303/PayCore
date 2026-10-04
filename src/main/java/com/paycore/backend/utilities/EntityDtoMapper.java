package com.paycore.backend.utilities;

import com.paycore.backend.dtos.responses.CustomerDetailsResponse;
import com.paycore.backend.dtos.responses.MerchantDetailResponse;
import com.paycore.backend.dtos.responses.PaymentDetails;
import com.paycore.backend.dtos.responses.TransactionDetails;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.entities.Payment;
import com.paycore.backend.entities.Transaction;
import org.springframework.stereotype.Component;


@Component
public class EntityDtoMapper {

    public CustomerDetailsResponse customerEntityDtoMapper(Customer customer){
        return new CustomerDetailsResponse(
                customer.getId(),
                customer.getName(),
                customer.getCreatedAt(),
                customer.getStatus()
        );
    }

    public MerchantDetailResponse merchantEntityDtoMapper(Merchant merchant){
         return  new MerchantDetailResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getCategory(),
                merchant.getStatus(),
                merchant.getCreatedAt()
        );
    }

    public PaymentDetails paymentEntityDtoMapper(Payment payment){

        CustomerDetailsResponse customerInfos =  customerEntityDtoMapper(payment.getCustomer());
        MerchantDetailResponse merchantInfos =  merchantEntityDtoMapper(payment.getMerchant());

        PaymentDetails response = new PaymentDetails(
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

    public TransactionDetails transactionEntityDtoMapper(Transaction transaction){
        return  new TransactionDetails(
              transaction.getId(),
              transaction.getType(),
              transaction.getStatus(),
              transaction.getAmount(),
              transaction.getFee(), transaction.getCreatedAt()
        );
    }


}
