package com.paycore.backend.services;


import com.paycore.backend.dtos.requests.CreatePaymentRequest;
import com.paycore.backend.dtos.responses.PaymentResponse;
import com.paycore.backend.dtos.responses.ProcessPaymentResponse;
import com.paycore.backend.dtos.responses.TransactionResponse;
import com.paycore.backend.processors.ProcessingResult;
import com.paycore.backend.entities.Customer;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.entities.Payment;
import com.paycore.backend.entities.Transaction;
import com.paycore.backend.enums.*;
import com.paycore.backend.exceptions.custom.InactiveResourceException;
import com.paycore.backend.exceptions.custom.ResourceNotFoundException;
import com.paycore.backend.processors.impl.BankTransferProcessor;
import com.paycore.backend.processors.impl.CardPaymentProcessor;
import com.paycore.backend.processors.impl.WalletPaymentProcessor;
import com.paycore.backend.repositories.CustomerRepository;
import com.paycore.backend.repositories.MerchantRepository;
import com.paycore.backend.repositories.PaymentRepository;
import com.paycore.backend.repositories.TransactionRepository;
import com.paycore.backend.utilities.EntityDtoMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final BankTransferProcessor bankTransferProcessor;
    private final CardPaymentProcessor cardPaymentProcessor;
    private final WalletPaymentProcessor walletPaymentProcessor;
    private final TransactionRepository transactionInterface;


    public PaymentService(PaymentRepository  paymentRepository,
                          MerchantRepository merchantRepository,
                          CustomerRepository customerRepository,
                          EntityDtoMapper entityDtoMapper,
                          BankTransferProcessor bankTransferProcessor,
                          CardPaymentProcessor cardPaymentProcessor,
                          WalletPaymentProcessor walletPaymentProcessor,
                          TransactionRepository transactionInterface) {
        this.paymentRepository = paymentRepository;
        this.merchantRepository = merchantRepository;
        this.customerRepository = customerRepository;
        this.entityDtoMapper = entityDtoMapper;
        this.bankTransferProcessor = bankTransferProcessor;
        this.cardPaymentProcessor = cardPaymentProcessor;
        this.walletPaymentProcessor = walletPaymentProcessor;
        this.transactionInterface = transactionInterface;
    }

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Merchant merchant = merchantRepository.findById(request.merchantId())
                .orElseThrow(()  -> new ResourceNotFoundException(
                        "Merchant " + request.merchantId() + " not found")
                );

        Customer customer = customerRepository.findById(request.customerID())
                .orElseThrow(()  -> new ResourceNotFoundException(
                        "Customer " + request.customerID() + " not found")
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
        PaymentResponse response = entityDtoMapper.paymentEntityDtoMapper(payment);

        return response;
    }

    @Transactional
    public ProcessPaymentResponse processPayment(UUID paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()  -> new ResourceNotFoundException(
                        "Payment " + paymentId + " not found")
                );

        payment.startProcessing();
        ProcessingResult processingResult  = switch (payment.getMethod()) {
            case PaymentMethod.BANK_TRANSFER -> bankTransferProcessor.processPayment(payment);
            case PaymentMethod.CARD -> cardPaymentProcessor.processPayment(payment);
            case PaymentMethod.WALLET -> walletPaymentProcessor.processPayment(payment);
        };

        if (processingResult.status() == PaymentStatus.SUCCESS){
            payment.markSuccesful();
        }else if  (processingResult.status() == PaymentStatus.FAILED){
            payment.markFailed();
        }
        paymentRepository.save(payment);

        TransactionStatus transactionStatus = processingResult.status() == PaymentStatus.SUCCESS
                ? TransactionStatus.SUCCESS : TransactionStatus.FAILED;

        Transaction processingTransaction = new Transaction(
                payment,
                TransactionType.PAYMENT,
                payment.getAmount(),
                processingResult.fee(),
                transactionStatus
        );
        transactionInterface.save(processingTransaction);
        TransactionResponse transactionRes = new TransactionResponse(
                processingTransaction.getId(),
                transactionStatus,
                processingResult.fee()
        );

        ProcessPaymentResponse processPaymentRes = new ProcessPaymentResponse(
                paymentId,
                processingResult.status(),
                transactionRes
        );
        return processPaymentRes;
    }

}
