package com.simplificado.bicpay.application.repository;

import com.simplificado.bicpay.domain.Transaction;
import com.simplificado.bicpay.domain.Wallet;

public interface WalletRepository {
    Wallet getById(String input);
    void update(Wallet input);
}
