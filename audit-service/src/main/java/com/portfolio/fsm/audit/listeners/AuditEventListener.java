package com.portfolio.fsm.audit.listeners;

import com.portfolio.fsm.audit.models.AuditEvent;
import com.portfolio.fsm.audit.repositories.AuditEventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuditEventListener {

    @Autowired
    private AuditEventRepository auditEventRepository;

    // This method will be triggered automatically whenever a message arrives in the "audit.queue"
    @RabbitListener(queues = "audit.queue")
    public void receiveMessage(Map<String, Object> payload) {
        System.out.println("Received Audit Event: " + payload);

        // Extract basic data from the JSON map
        String eventType = (String) payload.getOrDefault("eventType", "UNKNOWN");
        long timestamp = payload.containsKey("timestamp") ? (long) payload.get("timestamp") : System.currentTimeMillis();

        // Create the MongoDB Document and save it
        AuditEvent event = new AuditEvent(eventType, payload, timestamp);
        auditEventRepository.save(event);

        System.out.println("Successfully saved audit event to MongoDB with ID: " + event.getId());
    }
}
