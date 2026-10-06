package com.paycore.backend.utilities;

import com.paycore.backend.dtos.responses.CustomerDetailsResponse;
import com.paycore.backend.dtos.responses.MerchantDetailResponse;
import com.paycore.backend.dtos.responses.PaymentDetailsResponse;
import com.paycore.backend.dtos.responses.TransactionDetailsResponse;
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

    public PaymentDetailsResponse paymentEntityDtoMapper(Payment payment){

        CustomerDetailsResponse customerInfos =  customerEntityDtoMapper(payment.getCustomer());
        MerchantDetailResponse merchantInfos =  merchantEntityDtoMapper(payment.getMerchant());

        return new PaymentDetailsResponse(
                payment.getId(),
                customerInfos,
                merchantInfos,
                payment.getAmount(),
                payment.getRefundAmount(),
                payment.getCurrency(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getProcessedAt(),
                payment.getRefundedAt()
        );
    }

    public TransactionDetailsResponse transactionEntityDtoMapper(Transaction transaction){
        return  new TransactionDetailsResponse(
              transaction.getId(),
              transaction.getType(),
              transaction.getStatus(),
              transaction.getAmount(),
              transaction.getFee(), transaction.getCreatedAt()
        );
    }


}
