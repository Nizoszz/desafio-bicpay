package com.simplificado.bicpay.infra.gateway.exception;

public class NotificationProcessingException extends RuntimeException{
    public NotificationProcessingException(String message) {
        super(message);
    }
}
