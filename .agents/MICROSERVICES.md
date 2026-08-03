# Microservices Overview

## API Gateway

Responsibilities:

- Request routing
- Authentication
- Rate limiting
- API aggregation

---

## Authentication Service

Responsibilities:

- User authentication
- JWT generation
- Refresh tokens

Database:

PostgreSQL

---

## User Service

Responsibilities:

- User management
- Roles
- Permissions

Database:

PostgreSQL

---

## Customer Service

Responsibilities:

- Customer registration
- Customer information

Database:

PostgreSQL

---

## Work Order Service

Responsibilities:

- Work order lifecycle
- Assignment
- Scheduling
- Business rules

Database:

PostgreSQL

---

## Notification Service

Responsibilities:

- Email notifications
- Push notifications

Database:

None

Communication:

RabbitMQ

---

## Audit Service

Responsibilities:

- Event history
- Audit logs
- Activity tracking

Database:

MongoDB
