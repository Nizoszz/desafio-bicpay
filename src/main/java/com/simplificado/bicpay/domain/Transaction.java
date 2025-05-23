package com.simplificado.bicpay.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final UUID transactionId;
    private final UUID payer;
    private final UUID payee;
    private final BigDecimal value;
    private final LocalDateTime createdAt;

    public Transaction(UUID transactionId, UUID payer, UUID payee, double value, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.payer = payer;
        this.payee = payee;
        this.value = BigDecimal.valueOf(value);
        this.createdAt = createdAt;
    }
    public static Transaction create(String payer, String payee, double value){
        var trasactionId = UUID.randomUUID();
        return new Transaction(trasactionId, UUID.fromString(payer), UUID.fromString(payee), value, LocalDateTime.now());
    }

    public BigDecimal getValue() {
        return value;
    }

    public UUID getPayee() {
        return payee;
    }

    public UUID getPayer() {
        return payer;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
