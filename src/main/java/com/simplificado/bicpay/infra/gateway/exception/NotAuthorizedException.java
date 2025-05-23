package com.simplificado.bicpay.infra.gateway.exception;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException (String message) {
        super(message);
    }
}
