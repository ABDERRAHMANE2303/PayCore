package com.paycore.backend.services;


import com.paycore.backend.dtos.requests.CreatePaymentRequest;
import com.paycore.backend.dtos.requests.CreateRefundRequest;
import com.paycore.backend.dtos.responses.PaymentDetails;
import com.paycore.backend.dtos.responses.PaymentResponse;
import com.paycore.backend.dtos.responses.TransactionDetails;
import com.paycore.backend.exceptions.custom.InvalidOrderByOptionException;
import com.paycore.backend.exceptions.custom.InvalidSortOptionException;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private final TransactionRepository transactionRepository;


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
        this.transactionRepository = transactionInterface;
    }


    private Payment createPaymentEntity(CreatePaymentRequest request) {
        Merchant merchant = merchantRepository.findById(request.merchantId())
                .orElseThrow(()  -> new ResourceNotFoundException(
                        "Merchant " + request.merchantId() + " not found")
                );

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(()  -> new ResourceNotFoundException(
                        "Customer " + request.customerId()+ " not found")
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

        return payment;
    }


    private Transaction processPayment(UUID paymentId){
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
            payment.markSuccessful();
        }else if  (processingResult.status() == PaymentStatus.FAILED){
            payment.markFailed();
        }

        TransactionStatus transactionStatus = processingResult.status() == PaymentStatus.SUCCESS
                ? TransactionStatus.SUCCESS : TransactionStatus.FAILED;

        Transaction processingTransaction = new Transaction(
                payment,
                TransactionType.PAYMENT,
                payment.getAmount(),
                processingResult.fee(),
                transactionStatus
        );

        return processingTransaction;
    }

    @Transactional
    public PaymentResponse createAndProcessPayment(CreatePaymentRequest request){

        Payment payment = createPaymentEntity(request);
        paymentRepository.save(payment);
        Transaction transaction = processPayment(payment.getId());
        paymentRepository.save(payment);
        transactionRepository.save(transaction);

        PaymentDetails paymentDetails = entityDtoMapper.paymentEntityDtoMapper(payment);
        TransactionDetails transactionDetails = entityDtoMapper.transactionEntityDtoMapper(transaction);

        return new PaymentResponse(paymentDetails, transactionDetails);

    }

    @Transactional
    public PaymentResponse createRefund(CreateRefundRequest request,UUID paymentId){
        Payment payment = paymentRepository.findById(paymentId).
                orElseThrow(()  -> new ResourceNotFoundException(
                        "Payment " + paymentId + " not found")
                );

        BigDecimal refundAmount = request.amount();


        payment.startRefunding();
        payment.recordRefund(refundAmount);
        payment.markRefunded();

        Transaction transaction = new Transaction(
                payment,
                TransactionType.REFUND,
                refundAmount,
                BigDecimal.ZERO,
                TransactionStatus.SUCCESS
        );

        transactionRepository.save(transaction);

        PaymentDetails paymentDetails = entityDtoMapper.paymentEntityDtoMapper(payment);
        TransactionDetails transactionDetails = entityDtoMapper.transactionEntityDtoMapper(transaction);
        return new PaymentResponse(paymentDetails, transactionDetails);
    }

    @Transactional
    public PaymentDetails getPayment(UUID paymentId){
        Payment payment =  paymentRepository.findById(paymentId)
                .orElseThrow(()  -> new ResourceNotFoundException("" +
                "Payment " + paymentId + " not found")
                );
        return entityDtoMapper.paymentEntityDtoMapper(payment);
    }

    @Transactional
    public List<PaymentDetails> getPayments (
            UUID customerId,
            UUID merchantId,
            PaymentStatus status,
            PaymentMethod method,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String sortBy,
            String direction
    ){

        direction = direction.toLowerCase(Locale.ROOT);
        List<String> allowedSortOptions = List.of("amount","status","method","createdAt","refundedAt","processedAt");
        List<String> allowedDirections = List.of("asc","desc");
        if (!allowedSortOptions.contains(sortBy)) {
            throw new InvalidSortOptionException(
                    "you can only sort by : amount,status,method,createdAt,refundedAt and processedAt");
        }
        if (!allowedDirections.contains(direction)) {
            throw new InvalidOrderByOptionException(
                    "you can only sort by : asc,desc"
            );
        }

        Sort sort = Sort.by(
                Sort.Direction.fromString(direction),
                sortBy
        );

        List<Payment> payments = paymentRepository.search(
                customerId,
                merchantId,
                status,
                method,
                minAmount,
                maxAmount,
                sort
        );

        List<PaymentDetails> paymentDetailsList = new ArrayList<>();
        for (Payment payment : payments) {
            PaymentDetails paymentDetails = entityDtoMapper.paymentEntityDtoMapper(payment);
            paymentDetailsList.add(paymentDetails);
        }

        return paymentDetailsList;
    }

    @Transactional
    public List<TransactionDetails> getTransactions (UUID paymentId){

        if (!paymentRepository.existsById(paymentId)) {
            throw new ResourceNotFoundException(
                    "Payment " + paymentId + " not found"
            );
        }

        List<Transaction> transactions = transactionRepository.findByPayment_Id(paymentId);
        List<TransactionDetails> transactionDetailsList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDetails transactionDetails = entityDtoMapper.transactionEntityDtoMapper(transaction);
            transactionDetailsList.add(transactionDetails);
        }
        return transactionDetailsList;
    }



}
