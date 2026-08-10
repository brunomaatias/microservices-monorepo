package com.portfolio.fsm.user_service.events;

import com.portfolio.fsm.user_service.dto.EventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private static final String EXCHANGE = "fsm.exchange";
    private static final String ROUTING_KEY = "audit.event.user";

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(EventDto event) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
    }
}
