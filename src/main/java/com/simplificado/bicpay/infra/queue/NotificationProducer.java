package com.simplificado.bicpay.infra.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificado.bicpay.domain.TransactionEventNotification;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class NotificationProducer{
    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);
    private final SqsTemplate sqsTemplate;
    private final String queueName;
    private final ObjectMapper objectMapper;

    public NotificationProducer(SqsTemplate sqsTemplate, @Value("${aws.sqs.notification-queue}") String queueName, ObjectMapper objectMapper){
        this.sqsTemplate = sqsTemplate;
        this.queueName = queueName;
        this.objectMapper = objectMapper;
    }
    public void sendNotification(TransactionEventNotification message){
        try {
            String messageBody = objectMapper.writeValueAsString(message);
            log.info("Sending JSON message: {}", messageBody);
            sqsTemplate.send(
                    to -> to
                            .queue(queueName)
                            .payload(messageBody)
            );
            log.info("Message sent to queue: {}", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
