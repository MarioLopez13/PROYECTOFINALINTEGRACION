# Datos semilla y respaldo tecnico

El proyecto incluye dos mecanismos para crear datos de demostracion sin depender
de carga manual en base de datos.

## Script PowerShell

Con Docker Compose levantado y el gateway disponible en `http://localhost:8080`:

```powershell
.\scripts\seed-demo.ps1
```

El script crea un estudiante, matricula, obligacion de pago, confirmacion de pago,
asistencia e incidente. Tambien consulta el dashboard y el historial de eventos
del estudiante creado.

Parametros opcionales:

```powershell
.\scripts\seed-demo.ps1 -ApiBaseUrl http://localhost:8080 -ApiKey campus-demo-key
```

## Coleccion Postman

Importar:

```text
postman/CampusConnect360.postman_collection.json
```

Variables incluidas:

- `baseUrl`: `http://localhost:8080`
- `apiKey`: `campus-demo-key`
- `studentCode`: `STU-POSTMAN-001`
- `paymentCode`: `PAY-POSTMAN-001`

La coleccion cubre los flujos principales de la consigna: Secretaria, Finanzas,
Bienestar, Notificaciones/Resiliencia y Analytics.
