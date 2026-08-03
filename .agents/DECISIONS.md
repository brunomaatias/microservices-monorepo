# Architecture Decision Log

This document records the major architectural decisions made throughout the project.

---

## ADR-001

### Decision

Use RabbitMQ instead of Kafka.

### Reason

RabbitMQ provides a simpler setup and is sufficient for the expected workload of this project.

### Consequences

- Easier development
- Lower operational complexity
- Easier local environment

---

## ADR-002

### Decision

Each microservice owns its own database.

### Reason

Avoid tight coupling and allow services to evolve independently.

### Consequences

- Better scalability
- Independent deployments
- Eventual consistency

---

## ADR-003

### Decision

Use PostgreSQL for transactional data.

### Reason

Relational data requires strong consistency and transactional support.

---

## ADR-004

### Decision

Use MongoDB for audit logs.

### Reason

Audit data grows continuously and benefits from a flexible document model.

---

## ADR-005

### Decision

Use Data Transfer Objects (DTOs) for API Requests and Responses instead of raw JPA Entities.

### Reason

Exposing raw entities via APIs poses significant security risks (e.g., accidentally returning hashed passwords or internal database IDs to the frontend) and tightly couples the internal database schema to the external API contract. Using DTOs allows us to define exactly what the API should receive and return.

### Consequences

- Enhanced Security (Sensitive properties like passwords are automatically filtered out).
- Clear, stable API contracts that don't break if the database schema changes.
- Slightly more boilerplate code required for mapping Entities to DTOs.
