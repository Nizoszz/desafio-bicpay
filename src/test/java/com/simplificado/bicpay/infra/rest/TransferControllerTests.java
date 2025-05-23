package com.simplificado.bicpay.infra.rest;

import com.simplificado.bicpay.infra.gateway.AuthorizeTransactionGatewayImpl;
import com.simplificado.bicpay.infra.gateway.NotifyTransactionGatewayImpl;
import com.simplificado.bicpay.infra.queue.NotificationProducer;
import com.simplificado.bicpay.infra.rest.dto.RequestTransferDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data.sql")
public class TransferControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private RequestTransferDto requestTransferDto;

    @MockBean
    private AuthorizeTransactionGatewayImpl authorizeTransactionGateway;

    @MockBean
    private NotifyTransactionGatewayImpl notifyTransactionGateway;

    @MockBean
    private NotificationProducer notificationProducer;

    @BeforeEach
    void setup() {
        requestTransferDto = new RequestTransferDto(
                "01f092dc-f8b8-4868-8ccc-a0f49ea74305",
                "f6f41b6e-9009-4187-a62f-01751e8a4840",
                100
        );
        BDDMockito.given(authorizeTransactionGateway.authorize(any())).willReturn(true);
        BDDMockito.doNothing()
                .when(notificationProducer).sendNotification(any());
    }

    @Test
    @DisplayName("Must try to execute a successful transfer")
    public void testSuccessfulTransfer() {
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/api/v1/transfer",
                requestTransferDto,
                String.class);
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Assertions.assertFalse(Objects.requireNonNull(response.getBody()).isBlank());
    }
}
