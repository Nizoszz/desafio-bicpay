package com.simplificado.bicpay.application.gateway;

import com.simplificado.bicpay.domain.Transaction;

public interface AuthorizeTransactionGateway {
    boolean authorize(Transaction input);
}
