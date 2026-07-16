# CampusConnect360

Proyecto integrador de Integracion de Sistemas para una plataforma educativa
basada en microservicios, eventos y un frontend operativo por roles.

## Que resuelve

CampusConnect360 integra los procesos principales de una institucion educativa:

- Secretaria Academica: estudiantes, matriculas e historial de eventos.
- Finanzas: obligaciones de pago y confirmacion de pagos.
- Bienestar/Asistencia: registros de asistencia e incidentes.
- Notificaciones: mensajes generados por eventos del ecosistema.
- Direccion: dashboard consolidado con indicadores operativos.

La solucion evidencia integracion sincrona por REST y asincrona por RabbitMQ,
con API Gateway, resiliencia basica, DLQ, idempotencia, health checks y
documentacion tecnica.

## Arquitectura

- Frontend: React + Vite.
- Gateway: Spring Cloud Gateway con seguridad por `X-API-Key`.
- Microservicios: Spring Boot + Maven + arquitectura por capas.
- Persistencia: MySQL, una base de datos por microservicio.
- Mensajeria: RabbitMQ con exchange, colas, retry/DLQ e idempotencia.
- Contenedores: Docker y Docker Compose.

Servicios incluidos:

```text
backend/gateway
backend/services/academic-service
backend/services/payment-service
backend/services/attendance-service
backend/services/notification-service
backend/services/analytics-service
frontend
```

## Como levantar el proyecto

Requisitos:

- Docker Desktop
- Java 21 o superior
- Maven
- Node.js 20 o superior

Crear variables locales:

```powershell
Copy-Item .env.example .env
```

Levantar todo con Docker:

```powershell
docker compose up --build
```

URLs principales:

- Frontend: http://localhost:5173
- API Gateway: http://localhost:8080
- RabbitMQ Management: http://localhost:15672

Credenciales y valores demo:

- RabbitMQ: `campus` / `campus`
- API Key: `campus-demo-key`
- Header requerido: `X-API-Key: campus-demo-key`

## Datos de demostracion

Para precargar un flujo completo por API despues de levantar Docker:

```powershell
.\scripts\seed-demo.ps1
```

Tambien se incluye una coleccion Postman:

```text
postman/CampusConnect360.postman_collection.json
```

La coleccion cubre health checks, Secretaria, Finanzas, Bienestar,
Notificaciones/Resiliencia y Analytics.

## Guion de demo

1. Abrir el frontend en http://localhost:5173.
2. Registrar o cargar datos demo desde Secretaria Academica.
3. Confirmar un pago desde Finanzas.
4. Registrar asistencia o incidente desde Bienestar.
5. Revisar notificaciones generadas por eventos.
6. Revisar el dashboard directivo.
7. Abrir RabbitMQ para evidenciar exchange, colas y DLQ.

Guion ampliado: `docs/demo-guide.md`.

## Documentacion

- `docs/architecture/README.md`
- `docs/demo-guide.md`
- `docs/seed-data.md`
- `docs/bitacora.md`
- `contracts/events.md`
- `postman/CampusConnect360.postman_collection.json`

## Estado frente a la consigna

El proyecto cubre las fases principales solicitadas para el Progreso 3:

- Arquitectura distribuida por microservicios.
- Integracion entre servicios por REST y RabbitMQ.
- Bases de datos separadas.
- Gateway de entrada con API key.
- Dashboard y frontend de operacion.
- Manejo de eventos, idempotencia, reintentos y DLQ.
- Contenedores Docker.
- Evidencias de prueba con script semilla y Postman.

## Verificacion local

Comandos usados para validar:

```powershell
npm --prefix frontend run build
mvn -f backend/gateway/pom.xml test
mvn -f backend/services/academic-service/pom.xml test
mvn -f backend/services/payment-service/pom.xml test
mvn -f backend/services/attendance-service/pom.xml test
mvn -f backend/services/notification-service/pom.xml test
mvn -f backend/services/analytics-service/pom.xml test
```
