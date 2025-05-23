package com.simplificado.bicpay.application.usecase;

import com.simplificado.bicpay.application.gateway.AuthorizeTransactionGateway;
import com.simplificado.bicpay.application.repository.TransactionRepository;
import com.simplificado.bicpay.application.repository.WalletRepository;
import com.simplificado.bicpay.domain.Transaction;
import com.simplificado.bicpay.domain.TransactionEventNotification;
import com.simplificado.bicpay.domain.Wallet;
import com.simplificado.bicpay.infra.queue.NotificationProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class Transfer {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizeTransactionGateway authorizeTransactionGateway;
    private final NotificationProducer notificationProducer;

    public Transfer(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizeTransactionGateway authorizeTransactionGateway, NotificationProducer notificationProducer) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizeTransactionGateway = authorizeTransactionGateway;
        this.notificationProducer = notificationProducer;
    }

    @Transactional
    public Map<String, String> execute(Transaction input) {
        Wallet payer = walletRepository.getById(input.getPayer().toString());
        Wallet payee = walletRepository.getById(input.getPayee().toString());
        if(!"common".equalsIgnoreCase(payer.getWalletRole())) throw new IllegalArgumentException("The shopkeeper cannot make a transfer");
        authorizeTransactionGateway.authorize(input);
        payer.subtractBalance(input.getValue());
        payee.addBalance(input.getValue());
        walletRepository.update(payer);
        walletRepository.update(payee);
        transactionRepository.save(input);
        notificationProducer.sendNotification(
                new TransactionEventNotification(
                        input.getTransactionId(),
                        input.getPayer(),
                        input.getPayee()
                )
        );

        return Map.of("transactionId", input.getTransactionId().toString());
    }
}