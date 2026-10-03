package com.paycore.backend.services;


import com.paycore.backend.dtos.Requests.CreatePaymentReq;
import com.paycore.backend.dtos.Responses.PaymentInfosRes;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.entities.Payment;
import com.paycore.backend.enums.CustomerStatus;
import com.paycore.backend.enums.MerchantStatus;
import com.paycore.backend.exceptions.custom.InactiveResourceException;
import com.paycore.backend.exceptions.custom.ResourceNotFoundException;
import com.paycore.backend.repositories.CustomerRepository;
import com.paycore.backend.repositories.MerchantRepository;
import com.paycore.backend.repositories.PaymentRepository;
import com.paycore.backend.utilities.EntityDtoMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final EntityDtoMapper entityDtoMapper;


    public PaymentService(PaymentRepository  paymentRepository,
                          MerchantRepository merchantRepository,
                          CustomerRepository customerRepository,
                          EntityDtoMapper entityDtoMapper) {
        this.paymentRepository = paymentRepository;
        this.merchantRepository = merchantRepository;
        this.customerRepository = customerRepository;
        this.entityDtoMapper = entityDtoMapper;

    }

    @Transactional
    public PaymentInfosRes createPayment(CreatePaymentReq request) {
        Merchant merchant = merchantRepository.findById(request.merchantID())
                .orElseThrow(()  -> new ResourceNotFoundException(
                        "Merchant " + request.merchantID() + " not found")
                );

        Customer customer = customerRepository.findById(request.customerID())
                .orElseThrow(()  -> new ResourceNotFoundException(
                        "Customer " + request.merchantID() + " not found")
                );

        if (merchant.getStatus() == MerchantStatus.INACTIVE ){
            throw new InactiveResourceException(
                    "Merchant must be active to proceed with the payment");
        }

        if (customer.getStatus() == CustomerStatus.INACTIVE ){
            throw new InactiveResourceException(
                    "customer must be ACTIVE to proceed with the payment");
        }

        Payment payment = new Payment(
                customer,
                merchant,
                request.amount(),
                request.currency(),
                request.method()
        );

        paymentRepository.save(payment);
        PaymentInfosRes response = entityDtoMapper.paymentEntityDtoMapper(payment);

        return response;
    }

}
