# System Architecture

The application follows a Microservices Architecture.

Each service is responsible for a single business domain and owns its own database.

## Services

- API Gateway
- Authentication Service
- User Service
- Customer Service
- Work Order Service
- Notification Service
- Audit Service

## Communication

### Synchronous

REST APIs

Used when an immediate response is required.

Examples:

- User Authentication
- Fetch User Profile
- Retrieve Work Orders

### Asynchronous

RabbitMQ

Used for domain events and background processing.

Examples:

- Work Order Created
- Work Order Completed
- Notification Sent
- Audit Log Created

## Database Strategy

Each microservice owns its own database.

No database should be shared between services.

Services communicate only through REST APIs or asynchronous events.

## Architecture Principles

- Domain-driven design
- Loose coupling
- High cohesion
- Single Responsibility Principle
- Event-driven communication
- Independent deployment
- Fault isolation
