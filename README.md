# CampusConnect360

Base técnica para un sistema universitario con microservicios Spring Boot, un
frontend React y comunicación mediante Spring Cloud Gateway y RabbitMQ.

No contiene lógica de negocio.

## Requisitos

- Java 21
- Maven 3.9+
- Node.js 22+
- Docker y Docker Compose

## Inicio rápido

```powershell
Copy-Item .env.example .env
docker compose up --build
```

Frontend: http://localhost:5173  
Gateway health: http://localhost:8080/actuator/health  
RabbitMQ management: http://localhost:15672
