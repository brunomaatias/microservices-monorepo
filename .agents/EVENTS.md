# Domain Events

The system follows an Event-Driven Architecture.

Business events are published to RabbitMQ whenever an important action occurs.

## Events

### UserRegistered

Published when a new user is created.

Consumers:

- Audit Service
- Notification Service

---

### WorkOrderCreated

Published when a new work order is created.

Consumers:

- Notification Service
- Audit Service

---

### WorkOrderAssigned

Published when a technician is assigned.

Consumers:

- Notification Service

---

### WorkOrderStarted

Published when a technician starts a service.

Consumers:

- Audit Service

---

### WorkOrderCompleted

Published when a service is completed.

Consumers:

- Notification Service
- Audit Service

---

### WorkOrderCancelled

Published when a work order is cancelled.

Consumers:

- Notification Service
- Audit Service
