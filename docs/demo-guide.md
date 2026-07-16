# Guia de demostracion

## Preparacion

```powershell
Copy-Item .env.example .env
docker compose up --build
```

Opcionalmente, crear datos semilla por API:

```powershell
.\scripts\seed-demo.ps1
```

Abrir:

- Frontend: http://localhost:5173
- RabbitMQ: http://localhost:15672

Respaldo tecnico:

- Postman: `postman/CampusConnect360.postman_collection.json`
- Datos semilla: `docs/seed-data.md`

## Historia: un dia de operacion

1. En Secretaria, registrar estudiante y matricula. Alternativa: usar `Demo rapida`.
2. Confirmar que aparece el evento `StudentEnrolled` en la ficha del estudiante.
3. En Finanzas, registrar una deuda para el estudiante.
4. Confirmar el pago pendiente.
5. Volver a Secretaria y revisar estado financiero `PAID`.
6. En Bienestar, registrar asistencia.
7. Registrar un incidente de severidad media o alta.
8. Revisar notificaciones generadas.
9. Abrir Dashboard y validar indicadores consolidados.
10. Abrir trazabilidad y explicar `correlationId`, eventos y consumidores.

## Escenario de falla controlada

1. En Bienestar, pulsar `Fallar siguiente notificacion`.
2. Registrar un incidente nuevo.
3. Notification Service guarda una notificacion con estado `FAILED`.
4. Analytics incrementa `Mensajes fallidos`.
5. En RabbitMQ, revisar la cola `notification.dead-letter`.

## Preguntas tecnicas esperadas

- Por que hay una base de datos por servicio.
- Como funciona el exchange topic `campus.events`.
- Que consumidores reaccionan al mismo evento.
- Donde se evidencia idempotencia.
- Que pasa cuando Notification falla.
- Como el dashboard se alimenta sin consultar directamente todas las bases.
