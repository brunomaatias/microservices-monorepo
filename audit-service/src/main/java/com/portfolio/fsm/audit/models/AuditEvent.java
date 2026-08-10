package com.portfolio.fsm.audit.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "audit_events")
public class AuditEvent {

    @Id
    private String id;
    private String eventType;
    private Map<String, Object> payload;
    private long timestamp;

    public AuditEvent() {
    }

    public AuditEvent(String eventType, Map<String, Object> payload, long timestamp) {
        this.eventType = eventType;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
