# SpringBootKafkaMicroService Project

Enterprise-grade reference project for **Spring Boot Microservices + Kafka + in-memory storage + Docker + AKS + CI/CD + Observability**.

## Modules

| Module | Purpose |
|---|---|
| `common-events` | Shared event DTOs and constants |
| `order-producer-service` | REST API that stores an order and publishes an event to Kafka |
| `order-consumer-service` | Kafka listener that consumes events and stores read-model data in memory |
| `api-gateway` | Spring Cloud Gateway entry point |

## Tech Stack

- Java 21
- Spring Boot 3.4.x
- Spring Kafka
- Thread-safe in-memory repositories (data resets when a service restarts)
- Spring Cloud Gateway
- Docker and Docker Compose
- Kubernetes manifests for AKS
- GitHub Actions CI/CD for Azure Container Registry and AKS
- Azure DevOps pipeline sample
- Actuator, Prometheus, OpenTelemetry-ready tracing configuration

## Local Run

```bash
cd SpringBootKafkaMicroService_Project
docker compose up -d
mvn clean package
mvn -pl order-producer-service spring-boot:run
mvn -pl order-consumer-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

Create an order:

```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"CUST-1001","product":"Laptop","quantity":1,"amount":125000}'
```

Gateway route:

```bash
curl -X POST http://localhost:8080/orders/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"CUST-1002","product":"Monitor","quantity":2,"amount":30000}'
```

## Observability URLs

- Producer health: http://localhost:8081/actuator/health
- Consumer health: http://localhost:8082/actuator/health
- Gateway health: http://localhost:8080/actuator/health
- Producer Prometheus metrics: http://localhost:8081/actuator/prometheus
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

## AKS Deployment

Update these placeholders before deploying:

- `youracr.azurecr.io`
- `springboot-kafka-ms`
- Kafka bootstrap servers

```bash
kubectl apply -k k8s/base
```

## CI/CD Setup

GitHub repository secrets expected by `.github/workflows/ci-cd-aks.yml`:

- `AZURE_CLIENT_ID`
- `AZURE_TENANT_ID`
- `AZURE_SUBSCRIPTION_ID`
- `AZURE_RESOURCE_GROUP`
- `AKS_CLUSTER_NAME`
- `ACR_NAME`

The workflow uses Azure login, builds Docker images, pushes to Azure Container Registry, sets AKS context, then deploys Kubernetes manifests.

## Production Hardening Checklist

- Replace the in-memory repositories with a persistent database for production
- Use managed Kafka or Confluent Cloud / Azure Event Hubs Kafka endpoint
- Store secrets in Azure Key Vault and mount via Secrets Store CSI Driver
- Enable workload identity for AKS
- Configure private networking, ingress TLS, WAF, and network policies
- Add contract tests, integration tests, SAST, dependency scanning, and SBOM generation
- Configure Grafana dashboards and alert rules
