package com.simplificado.bicpay.infra.rest.dto;

public record RequestTransferDto(
        String payer,
        String payee,
        double value
){
}
