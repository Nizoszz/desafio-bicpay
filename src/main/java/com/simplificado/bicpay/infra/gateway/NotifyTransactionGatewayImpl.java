package com.simplificado.bicpay.infra.gateway;

import com.simplificado.bicpay.application.gateway.NotifyTransactionGateway;
import com.simplificado.bicpay.infra.gateway.exception.NotificationProcessingException;
import com.simplificado.bicpay.infra.queue.NotificationConsumer;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
public class NotifyTransactionGatewayImpl implements NotifyTransactionGateway{
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final RestTemplate restTemplate;

    public NotifyTransactionGatewayImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean notifyTransaction() {
        try {
            restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", null, Map.class);
            return true;
        } catch (HttpServerErrorException e){
            return false;
        }
    }
}
