package com.simplificado.bicpay.application.usecase;

import com.simplificado.bicpay.domain.Transaction;
import com.simplificado.bicpay.infra.gateway.AuthorizeTransactionGatewayImpl;
import com.simplificado.bicpay.infra.gateway.NotifyTransactionGatewayImpl;
import com.simplificado.bicpay.infra.queue.NotificationConsumer;
import com.simplificado.bicpay.infra.queue.NotificationProducer;
import com.simplificado.bicpay.infra.repository.TransactionRepositoryDatabase;
import com.simplificado.bicpay.infra.repository.WalletRepositoryDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;


import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data.sql")
public class TransferTests {
    private Transfer transfer;
    private GetWallet getWallet;
    @Autowired
    private TransactionRepositoryDatabase transactionRepositoryDatabase;
    @Autowired
    private WalletRepositoryDatabase walletRepositoryDatabase;
    @MockBean
    private AuthorizeTransactionGatewayImpl authorizeTransactionGateway;
    @MockBean
    private NotificationProducer notificationProducer;
    @MockBean
    private NotificationConsumer notificationConsumer;
    @MockBean
    private NotifyTransactionGatewayImpl notifyTransactionGateway;

    @BeforeEach
    public void setUp(){
        transfer = new Transfer(transactionRepositoryDatabase, walletRepositoryDatabase, authorizeTransactionGateway, notificationProducer);
        getWallet = new GetWallet(walletRepositoryDatabase);
    }
    @Test
    @DisplayName("Must try to execute a successful transfer")
    void mustTryToExecuteASuccessfullyTransfer() throws RuntimeException {
        BDDMockito.given(authorizeTransactionGateway.authorize(any())).willReturn(true);
        BDDMockito.doNothing()
                .when(notificationProducer).sendNotification(any());
        var transactionInput = Transaction.create(
                "01f092dc-f8b8-4868-8ccc-a0f49ea74305",
                "f6f41b6e-9009-4187-a62f-01751e8a4840",
                500.00
        );
        transfer.execute(transactionInput);
        var walletOutput = getWallet.execute("01f092dc-f8b8-4868-8ccc-a0f49ea74305");
        assertEquals(0, walletOutput.getBalance().compareTo(BigDecimal.valueOf(500.00)));
    }
    @Test
    @DisplayName("Must try to execute a transfer with a failed authorization")
    void mustTryToExecuteATransferWithAFailedAuthorization() throws RuntimeException {
        BDDMockito.willThrow(new RuntimeException("Failed authorization request"))
                .given(authorizeTransactionGateway)
                .authorize(any());
        var transactionInput = Transaction.create(
                "01f092dc-f8b8-4868-8ccc-a0f49ea74305",
                "f6f41b6e-9009-4187-a62f-01751e8a4840",
                500.00
        );
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transfer.execute(transactionInput);
        });

        assertEquals("Failed authorization request", exception.getMessage());
    }
    @Test
    @DisplayName("Must try to execute a transfer with a failed notification")
    void mustTryToExecuteATransferWithAFailedNotification() throws RuntimeException {
        BDDMockito.willThrow(new RuntimeException("Failed sender notification"))
                .given(notificationProducer)
                .sendNotification(any());
        var transactionInput = Transaction.create(
                "01f092dc-f8b8-4868-8ccc-a0f49ea74305",
                "f6f41b6e-9009-4187-a62f-01751e8a4840",
                500.00
        );
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transfer.execute(transactionInput);
        });

        assertEquals("Failed sender notification", exception.getMessage());
    }
}
