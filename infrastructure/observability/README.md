# Observability

Cada aplicacion expone Spring Boot Actuator en `/actuator/health`.

Puntos de revision durante la demo:

- Gateway: http://localhost:8080/actuator/health
- Academic: http://localhost:8081/actuator/health
- Payment: http://localhost:8082/actuator/health
- Attendance: http://localhost:8083/actuator/health
- Notification: http://localhost:8084/actuator/health
- Analytics: http://localhost:8085/actuator/health
- RabbitMQ Management: http://localhost:15672

La trazabilidad funcional se revisa en:

- `GET /api/academic/students/{studentCode}/events`
- `GET /api/analytics/events`
