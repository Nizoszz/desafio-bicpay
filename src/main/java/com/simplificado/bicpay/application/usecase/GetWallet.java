package com.simplificado.bicpay.application.usecase;

import com.simplificado.bicpay.application.repository.WalletRepository;
import com.simplificado.bicpay.domain.Wallet;
import com.simplificado.bicpay.infra.repository.WalletRepositoryDatabase;

public class GetWallet{
    private final WalletRepository walletRepository;

    public GetWallet(WalletRepository walletRepository){
        this.walletRepository = walletRepository;
    }

    public Wallet execute(String walletId){
       return this.walletRepository.getById(walletId);
    }
}
