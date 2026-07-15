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
# CampusConnect360 – Estado Actual del Proyecto (Fase 1)

## Proyecto

**CampusConnect360**
Materia: Integración de Sistemas

## Arquitectura elegida

Después de analizar varias alternativas, se decidió implementar el proyecto utilizando:

- Arquitectura de Microservicios
- Arquitectura por Capas dentro de cada microservicio

Cada microservicio será una aplicación Spring Boot independiente y se comunicará mediante:

- REST
- RabbitMQ (eventos)

El acceso desde el frontend será mediante un API Gateway.

---

# Tecnologías

## Backend

- Java 21
- Spring Boot 3
- Maven
- Spring Data JPA
- Spring Validation
- Spring Actuator
- Springdoc OpenAPI (Swagger)

## Frontend

- React
- Vite
- React Router

## Base de datos

- MySQL

Cada microservicio tendrá su propia base de datos.

## Integración

- RabbitMQ

## Infraestructura

- Docker
- Docker Compose

---

# Decisiones de arquitectura

NO se utilizará:

- Eureka
- Kubernetes
- Prometheus
- Grafana
- Integración Continua

Porque no forman parte del alcance inicial del proyecto.

---

# Estructura del proyecto

```
CampusConnect360/

backend/
    gateway/
    services/
        academic-service/
        payment-service/
        attendance-service/
        notification-service/
        analytics-service/

frontend/

infrastructure/
    mysql/
    rabbitmq/
    observability/

contracts/
    asyncapi/
    openapi/
    schemas/

docs/
    architecture/
    adr/
    diagrams/
    deployment/

scripts/

docker-compose.yml

README.md
```

---

# Arquitectura interna de cada microservicio

Cada microservicio utiliza Arquitectura por Capas.

```
src/main/java/ec/edu/udla/campusconnect/

controller/

service/

repository/

entity/

dto/

mapper/

messaging/

config/

exception/

Application.java
```

Todavía NO existen entidades ni lógica de negocio.

Únicamente se creó la estructura.

---

# Frontend

Se decidió utilizar UN SOLO proyecto React.

No existirán cuatro aplicaciones React independientes.

Dentro del frontend existirán módulos como:

```
features/

academic/

payments/

attendance/

notifications/

analytics/
```

---

# Lo que YA está implementado

✔ React + Vite

✔ Spring Cloud Gateway

✔ Cinco microservicios Spring Boot

✔ Dockerfiles

✔ Docker Compose

✔ RabbitMQ preparado

✔ MySQL preparado

✔ Swagger preparado

✔ Spring Actuator

✔ Health Checks

✔ Arquitectura por capas

✔ Estructura de carpetas definitiva

✔ .gitignore

✔ README

---

# Lo que NO está implementado todavía

NO existe lógica de negocio.

No existen:

- Estudiantes
- Matrículas
- Pagos
- Asistencia
- Incidentes
- Dashboard
- RabbitMQ funcional
- JWT
- CRUD

Todo eso se desarrollará por fases.

---

# Estado del proyecto

## Fase 1

COMPLETADA

Objetivo:

Crear toda la base técnica del proyecto.

Resultado:

Proyecto compilable con la arquitectura definitiva.

---

# Próxima fase

FASE 2

Implementar Academic Service.

Este será el primer microservicio funcional.

Debe contener:

- Student Entity
- Enrollment Entity
- DTO
- Repository
- Service
- Controller
- Swagger
- MySQL
- Validaciones
- Manejo de excepciones

AÚN NO conectar RabbitMQ.

Primero debe funcionar completamente el CRUD académico.

---

# Orden de desarrollo

Fase 2

Academic Service

↓

Fase 3

Payment Service

↓

Fase 4

RabbitMQ

↓

Fase 5

Notification Service

↓

Fase 6

Attendance Service

↓

Fase 7

Analytics Service

↓

Fase 8

Frontend

↓

Fase 9

Docker Compose final

↓

Fase 10

DLQ

Idempotencia

Logs

Health Checks

---

# Convenciones

Paquete base:

```
ec.edu.udla.campusconnect
```

Ejemplo:

```
ec.edu.udla.campusconnect.academic
```

---

# Importante

NO cambiar la estructura del proyecto.

NO crear nuevos microservicios.

NO dividir nuevamente el frontend.

NO cambiar nombres de carpetas.

Mantener exactamente la arquitectura acordada.

---

# Forma de trabajar con IA

Antes de generar código:

1. Analizar la arquitectura.
2. Explicar el plan.
3. Esperar aprobación.
4. Recién generar código.

No implementar varias funcionalidades simultáneamente.

Cada fase debe quedar funcionando antes de iniciar la siguiente.

---

# Objetivo de la siguiente sesión

Desarrollar completamente Academic Service y dejarlo funcionando con MySQL y Swagger para utilizarlo como plantilla del resto de microservicios.
