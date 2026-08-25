# Architecture

```text
Client
  |
  v
API Gateway
  |
  v
Order Producer Service ---> in-memory orders store
  |
  v
Kafka topic: order.created.v1
  |
  v
Order Consumer Service ---> in-memory order_read_model store
```

## Runtime Flow

1. Client posts an order request to the producer API.
2. Producer validates the request and persists order data in its in-memory store.
3. Producer publishes `OrderCreatedEvent` to Kafka.
4. Consumer listens on `order.created.v1`, processes the event, and updates a read model.
5. Actuator exposes health and Prometheus metrics for each service.
6. Kubernetes probes use `/actuator/health/liveness` and `/actuator/health/readiness`.

## Cloud Deployment

- Images are built in CI/CD and pushed to Azure Container Registry.
- AKS pulls images from ACR and runs the services.
- ConfigMap contains non-sensitive configuration.
- Secret contains sensitive connection strings.
- HPA scales producer and consumer deployments based on CPU utilization.
