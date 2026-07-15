# CampusConnect360 Fases 1-3 Design

## Alcance

El objetivo es avanzar el proyecto hasta la Fase 3 de forma incremental y defendible en GitHub. Cada fase debe quedar en un commit separado y subido al repositorio remoto para evidenciar progreso real.

## Fase 1: base tecnica

La base tecnica existente se mantiene: Spring Cloud Gateway, cinco microservicios Spring Boot, frontend React, Docker Compose, MySQL por servicio y RabbitMQ preparado. Esta fase cierra la validacion inicial, documenta el alcance y deja el repositorio listo para construir servicios funcionales.

## Fase 2: Academic Service

Academic Service sera el primer microservicio funcional. Debe permitir registrar, consultar, actualizar y eliminar estudiantes, ademas de crear y consultar matriculas. El modelo incluye `Student` y `Enrollment`, DTOs, repositorios JPA, servicios de dominio, controladores REST, validaciones y manejo uniforme de errores.

## Fase 3: Payment Service

Payment Service sera el segundo microservicio funcional. Debe permitir crear obligaciones de pago para un estudiante, confirmar pagos y consultar pagos por estado. El modelo incluye `Payment`, DTOs, repositorio JPA, servicio de dominio, controlador REST, validaciones y errores consistentes.

## Fuera de alcance por ahora

RabbitMQ funcional, eventos de negocio, JWT, notificaciones, asistencia, analitica y dashboard quedan para fases posteriores. En Fase 2 y Fase 3 se pueden dejar identificadores y campos preparados, pero no se implementa mensajeria real.

## Verificacion

Cada fase se verifica con pruebas automatizadas del modulo correspondiente. El frontend debe seguir compilando. Los commits se empujan a `origin/main` al terminar cada fase.
