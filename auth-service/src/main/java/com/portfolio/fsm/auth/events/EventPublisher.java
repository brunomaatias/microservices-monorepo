package com.portfolio.fsm.auth.events;

import com.portfolio.fsm.auth.dto.EventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private static final String EXCHANGE = "fsm.exchange";
    // Note that the routing key is different: audit.event.auth
    private static final String ROUTING_KEY = "audit.event.auth";

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(EventDto event) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
    }
}
