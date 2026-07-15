# Contratos de eventos

Todos los eventos publicados en RabbitMQ usan el exchange topic `campus.events`.

## Estructura comun

```json
{
  "eventId": "evt-uuid",
  "eventType": "StudentEnrolled",
  "occurredAt": "2026-07-15T10:30:00Z",
  "correlationId": "corr-uuid",
  "entityId": "STU-001",
  "data": {}
}
```

Campos obligatorios:

- `eventId`: identificador unico del evento.
- `eventType`: nombre del evento de negocio.
- `occurredAt`: fecha/hora de ocurrencia.
- `correlationId`: trazabilidad de extremo a extremo.
- `entityId`: entidad principal del evento.
- `data`: datos especificos del evento.

## Eventos

| Evento | Routing key | Productor | Consumidores |
| --- | --- | --- | --- |
| `StudentEnrolled` | `student.enrolled` | Academic | Notification, Analytics |
| `PaymentCreated` | `payment.created` | Payment | Analytics |
| `PaymentConfirmed` | `payment.confirmed` | Payment | Academic, Notification, Analytics |
| `StudentStatusUpdated` | `student.status-updated` | Academic | Analytics |
| `AttendanceRecorded` | `attendance.recorded` | Attendance | Notification, Analytics |
| `IncidentReported` | `incident.reported` | Attendance | Notification, Analytics |
| `NotificationSent` | `notification.sent` | Notification | Analytics |
| `NotificationFailed` | `notification.failed` | Notification | Analytics |

## Ejemplo `StudentEnrolled`

```json
{
  "eventId": "evt-1",
  "eventType": "StudentEnrolled",
  "occurredAt": "2026-07-15T10:30:00Z",
  "correlationId": "corr-1",
  "entityId": "STU-001",
  "data": {
    "studentCode": "STU-001",
    "studentName": "Ana Lopez",
    "representativeEmail": "ana.familia@example.com",
    "schoolId": "SCH-001",
    "grade": "8vo EGB",
    "academicYear": "2026-2027",
    "pendingAmount": 120.00
  }
}
```

## Ejemplo `PaymentConfirmed`

```json
{
  "eventId": "evt-2",
  "eventType": "PaymentConfirmed",
  "occurredAt": "2026-07-15T10:45:00Z",
  "correlationId": "corr-2",
  "entityId": "PAY-001",
  "data": {
    "paymentCode": "PAY-001",
    "studentCode": "STU-001",
    "description": "Matricula inicial",
    "amount": 120.00,
    "status": "CONFIRMED",
    "confirmationReference": "BANK-REF-001",
    "confirmedAt": "2026-07-15T10:45:00Z"
  }
}
```

## Colas y DLQ

| Cola | Binding | DLQ |
| --- | --- | --- |
| `academic.payment-confirmed` | `payment.confirmed` | `academic.dead-letter` |
| `notification.business-events` | `student.enrolled`, `payment.confirmed`, `attendance.recorded`, `incident.reported` | `notification.dead-letter` |
| `analytics.business-events` | `#` | `analytics.dead-letter` |
