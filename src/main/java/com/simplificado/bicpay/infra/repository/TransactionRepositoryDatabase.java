package com.simplificado.bicpay.infra.repository;

import com.simplificado.bicpay.application.repository.TransactionRepository;
import com.simplificado.bicpay.domain.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryDatabase implements TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    public TransactionRepositoryDatabase(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}
    @Override
    public void save(Transaction input) {
        String sql = "INSERT INTO TRANSACTIONS (TRANSACTION_ID, PAYER, PAYEE, \"VALUE\", CREATED_AT) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(sql, input.getTransactionId(), input.getPayer(), input.getPayee(), input.getValue());
    }
}
