package com.simplificado.bicpay.infra.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificado.bicpay.application.gateway.NotifyTransactionGateway;
import com.simplificado.bicpay.domain.TransactionEventNotification;
import com.simplificado.bicpay.infra.gateway.exception.NotificationProcessingException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer{
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final NotifyTransactionGateway notifyTransactionGateway;
    private final ObjectMapper objectMapper;
    private int count = 1;


    public NotificationConsumer(NotifyTransactionGateway notifyTransactionGateway, ObjectMapper objectMapper){
        this.notifyTransactionGateway = notifyTransactionGateway;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${aws.sqs.notification-queue}")
    public void processMessage(@Payload String messageBody){
        try {
            log.info("Raw message received: {}", messageBody);
            TransactionEventNotification message = objectMapper.readValue(
                    messageBody, TransactionEventNotification.class);
            log.info("Parsed message: {}", message);
            var sucess = notifyTransactionGateway.notifyTransaction();
            if(sucess){
                log.info("Process notification successfully: {}", message);
                System.out.println(count++);
            }
        } catch (NotificationProcessingException e) {
            log.error("Process notification error, message will be retried: {}", e.getMessage());
            throw e;
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message: {}", messageBody, e);
        }
    }
}
