package com.simplificado.bicpay.infra.repository;

import com.simplificado.bicpay.application.repository.WalletRepository;
import com.simplificado.bicpay.domain.Wallet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

@Repository
public class WalletRepositoryDatabase implements WalletRepository {
    private final JdbcTemplate jdbcTemplate;
    public WalletRepositoryDatabase(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}
    @Override
    public Wallet getById(String input) {
        String sql = "SELECT * FROM WALLETS WHERE WALLET_ID = ?";
        var rowSet = jdbcTemplate.queryForRowSet(sql, UUID.fromString(input));
        boolean found = rowSet.next();
        if(!found){ throw new RuntimeException("Wallet not found");}
        return new Wallet(UUID.fromString(
                Objects.requireNonNull(rowSet.getString("WALLET_ID"))),
                rowSet.getString("FULL_NAME"),
                rowSet.getString("TAX_NUMBER"),
                rowSet.getString("PASSWORD"),
                rowSet.getString("WALLET_ROLE"),
                rowSet.getBigDecimal("BALANCE"),
                rowSet.getInt("VERSION"));
    }
    @Override
    public void update(Wallet input) {
        String sql = "UPDATE WALLETS SET BALANCE = ? WHERE WALLET_ID = ?";
        jdbcTemplate.update(sql, input.getBalance(), input.getWalletId());
    }
}
