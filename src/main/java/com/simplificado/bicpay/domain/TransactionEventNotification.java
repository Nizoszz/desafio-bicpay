package com.simplificado.bicpay.domain;

public record TransactionEventNotification(
        java.util.UUID transactionId,
        java.util.UUID payer,
        java.util.UUID payee
){
}
