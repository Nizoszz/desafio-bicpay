package com.simplificado.bicpay.infra.gateway;

import com.simplificado.bicpay.application.gateway.AuthorizeTransactionGateway;
import com.simplificado.bicpay.domain.Transaction;
import com.simplificado.bicpay.infra.gateway.exception.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
public class AuthorizeTransactionGatewayImpl implements AuthorizeTransactionGateway {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public boolean authorize(Transaction input) {
        var outputResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        if (!outputResponse.hasBody() || outputResponse.getStatusCode() != HttpStatus.OK) {
            throw new NotAuthorizedException("Failed authorization request");
        }
        return "success".equalsIgnoreCase(Objects.requireNonNull(outputResponse.getBody()).get("status").toString());
    }
}
