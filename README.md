# CampusConnect 360

Ecosistema funcional de integracion para una red de colegios. El proyecto simula
un dia de operacion con Secretaria Academica, Finanzas, Bienestar/Docentes,
Notificaciones y Dashboard Directivo conectados por API Gateway, RabbitMQ y bases
de datos separadas.

## Estado implementado

- Frontend React operativo con portales de Secretaria, Finanzas, Bienestar y Dashboard.
- API Gateway Spring Cloud Gateway con seguridad por `X-API-Key`.
- Academic Service con estudiantes, matriculas, historial de eventos y actualizacion financiera.
- Payment Service con obligaciones de pago, pagos pendientes y confirmacion.
- Attendance Service con asistencia e incidentes.
- Notification Service consumidor de eventos, notificaciones simuladas y falla controlada con DLQ.
- Analytics Service como proyeccion CQRS para dashboard y trazabilidad.
- RabbitMQ topic exchange `campus.events` y dead letter exchange `campus.dlx`.
- Swagger/OpenAPI por servicio y health checks con Actuator.
- Docker Compose con MySQL independiente por servicio.

## Requisitos

- Java 21 o superior.
- Maven 3.9+.
- Node.js 22+.
- Docker y Docker Compose.

## Ejecucion con Docker Compose

```powershell
Copy-Item .env.example .env
docker compose up --build
```

URLs principales:

- Frontend: http://localhost:5173
- API Gateway: http://localhost:8080
- RabbitMQ Management: http://localhost:15672
- Academic Swagger: http://localhost:8081/swagger-ui.html
- Payment Swagger: http://localhost:8082/swagger-ui.html
- Attendance Swagger: http://localhost:8083/swagger-ui.html
- Notification Swagger: http://localhost:8084/swagger-ui.html
- Analytics Swagger: http://localhost:8085/swagger-ui.html

Credenciales y valores demo:

- RabbitMQ: `campus` / `campus`
- API Key: `campus-demo-key`
- Header requerido por gateway: `X-API-Key: campus-demo-key`
- Usuarios de prueba funcionales: las pestanas del frontend representan los roles
  Secretaria, Finanzas, Bienestar y Direccion. No hay login de usuario final; la
  seguridad basica se evidencia en el gateway con API key.

## Guion de demo

1. Abrir el frontend en http://localhost:5173.
2. En Secretaria, usar `Demo rapida` o registrar estudiante y matricula manualmente.
3. Verificar en la ficha el evento `StudentEnrolled`.
4. En Finanzas, crear una deuda y confirmar un pago.
5. Confirmar que Academic cambia el estado financiero a `PAID`.
6. En Bienestar, registrar asistencia o incidente.
7. Revisar notificaciones simuladas.
8. En Dashboard, revisar indicadores y trazabilidad de eventos.
9. Para resiliencia, pulsar `Fallar siguiente notificacion` y luego registrar un
   nuevo evento. Notification Service guarda `NotificationFailed` y el mensaje va
   a `notification.dead-letter`.

## Validacion local

Frontend:

```powershell
cd frontend
npm install
npm run lint
npm run build
```

Backend, desde cada modulo:

```powershell
mvn test
```

Modulos:

- `backend/gateway`
- `backend/services/academic-service`
- `backend/services/payment-service`
- `backend/services/attendance-service`
- `backend/services/notification-service`
- `backend/services/analytics-service`

## Servicios y eventos

| Servicio | Responsabilidad | Eventos |
| --- | --- | --- |
| Academic | Estudiantes, matriculas, estado financiero, historial | Publica `StudentEnrolled`, consume `PaymentConfirmed`, publica `StudentStatusUpdated` |
| Payment | Obligaciones y confirmacion de pagos | Publica `PaymentCreated`, `PaymentConfirmed` |
| Attendance | Asistencia, ausencias, atrasos e incidentes | Publica `AttendanceRecorded`, `IncidentReported` |
| Notification | Notificaciones simuladas e incidentes de entrega | Consume eventos de negocio, publica `NotificationSent`, `NotificationFailed` |
| Analytics | Vista consolidada y trazabilidad | Consume todos los eventos de `campus.events` |
| Gateway | Entrada centralizada y API key | Enruta `/api/**` |

Mas detalle en:

- `docs/architecture/README.md`
- `contracts/events.md`
- `docs/demo-guide.md`
- `docs/bitacora.md`
