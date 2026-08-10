package com.portfolio.fsm.audit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "fsm.exchange";
    public static final String AUDIT_QUEUE = "audit.queue";
    public static final String ROUTING_KEY = "audit.event.#";

    // 1. Create the Exchange (The "Mailroom" that routes messages)
    @Bean
    public TopicExchange fsmExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 2. Create the Queue (The "Inbox" for the Audit Service)
    @Bean
    public Queue auditQueue() {
        return new Queue(AUDIT_QUEUE, true); // true = durable (survives restarts)
    }

    // 3. Bind the Queue to the Exchange using a Routing Key pattern
    @Bean
    public Binding binding(Queue auditQueue, TopicExchange fsmExchange) {
        return BindingBuilder.bind(auditQueue).to(fsmExchange).with(ROUTING_KEY);
    }

    // 4. Use JSON for message serialization
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
