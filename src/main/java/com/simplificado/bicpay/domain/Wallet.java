package com.simplificado.bicpay.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet {
    private UUID walletId;
    private String fullName;
    private String taxNumber;
    private String email;
    private String password;
    private String walletRole;
    private BigDecimal balance;
    private int version;

    public Wallet(UUID walletId, String fullName, String taxNumber, String email, String walletRole, BigDecimal balance, int version) {
        this.walletId = walletId;
        this.fullName = fullName;
        this.taxNumber = taxNumber;
        this.email = email;
        this.walletRole = walletRole;
        this.balance = balance;
        this.version = version;
    }

    public Wallet() {
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getWalletRole() {
        return walletRole;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getEmail() {return email;}

    public String getFullName() {
        return fullName;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void addBalance(BigDecimal value){
        this.balance = this.balance.add(value);
    }

    public void subtractBalance(BigDecimal value){
        if(this.balance.compareTo(BigDecimal.ZERO) <= 0){throw new RuntimeException("Insufficient balance");}
        this.balance = this.balance.subtract(value);
    }
}
