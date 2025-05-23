package com.simplificado.bicpay.application.repository;

import com.simplificado.bicpay.domain.Transaction;

public interface TransactionRepository {
    void save(Transaction input);
}
