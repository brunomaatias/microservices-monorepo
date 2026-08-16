package com.portfolio.fsm.workorder.events;

import com.portfolio.fsm.workorder.dto.EventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class EventPublisher {

    private static final String EXCHANGE_NAME = "fsm.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishEvent(String routingKey, String eventType, Map<String, Object> payload) {
        EventDto event = new EventDto(
                UUID.randomUUID(),
                eventType,
                "work-order-service",
                System.currentTimeMillis(),
                payload
        );

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, event);
    }
}
