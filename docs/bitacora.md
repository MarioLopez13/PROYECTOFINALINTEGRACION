# Bitacora de trabajo

## Integrantes

- Completar con los integrantes del grupo.
- Responsable del avance documentado en estos commits: `mateop11`.

## Responsabilidades por componente

- Academic Service: estudiantes, matriculas, historial y actualizacion financiera.
- Payment Service: obligaciones y confirmacion de pago.
- Attendance Service: asistencia e incidentes.
- Notification Service: traduccion de eventos a notificaciones simuladas y DLQ.
- Analytics Service: proyeccion CQRS, indicadores y trazabilidad.
- Frontend: portales funcionales y dashboard.
- Gateway/Infraestructura: API key, Docker Compose, RabbitMQ y MySQL.

## Decisiones principales

- Mantener la arquitectura original de cinco servicios y un gateway.
- Usar RabbitMQ topic exchange para pub/sub.
- Usar DLQ para evidenciar resiliencia.
- Usar API key como mecanismo de seguridad basica.
- Usar una proyeccion analitica en lugar de consultar directamente cada servicio.

## Problemas encontrados

- El Maven instalado en PATH estaba roto. Para validar se uso un Maven portable en
  `work/tools`.
- La ejecucion paralela de Maven consumio demasiada memoria; se valido de forma
  secuencial con cache aislado.
- Los tests de contexto de servicios consumidores intentaban arrancar listeners
  RabbitMQ; se desactivo `spring.rabbitmq.listener.simple.auto-startup` solo en tests.

## Cambios importantes realizados

- Se agrego mensajeria RabbitMQ y contratos de eventos.
- Se implementaron Attendance, Notification y Analytics.
- Se conecto PaymentConfirmed con Academic para actualizar estado financiero.
- Se agrego frontend funcional con demo rapida.
- Se actualizo documentacion de arquitectura, eventos, demo y ejecucion.

## Herramientas utilizadas

- Java 21/Spring Boot 3.
- Maven.
- React/Vite.
- Docker Compose.
- RabbitMQ.
- MySQL.
- IA generativa como apoyo para implementacion, documentacion y validacion.

## Repositorio

https://github.com/MarioLopez13/PROYECTOFINALINTEGRACION
