param(
    [string]$ApiBaseUrl = "http://localhost:8080",
    [string]$ApiKey = "campus-demo-key"
)

$ErrorActionPreference = "Stop"

$suffix = Get-Date -Format "yyyyMMddHHmmss"
$studentCode = "STU-$suffix"
$paymentCode = "PAY-$suffix"

$headers = @{
    "Content-Type" = "application/json"
    "X-API-Key" = $ApiKey
}

function Invoke-CampusRequest {
    param(
        [string]$Method,
        [string]$Path,
        [object]$Body = $null
    )

    $params = @{
        Method = $Method
        Uri = "$ApiBaseUrl$Path"
        Headers = $headers
    }

    if ($null -ne $Body) {
        $params.Body = ($Body | ConvertTo-Json -Depth 8)
    }

    Invoke-RestMethod @params
}

Write-Host "Creating demo data with suffix $suffix"

Invoke-CampusRequest -Method "POST" -Path "/api/academic/students" -Body @{
    studentCode = $studentCode
    documentNumber = "DOC-$suffix"
    firstName = "Ana"
    lastName = "Lopez"
    birthDate = "2012-05-10"
    representativeEmail = "ana.lopez.$suffix@example.com"
    representativePhone = "0999999999"
} | Out-Null

Invoke-CampusRequest -Method "POST" -Path "/api/academic/enrollments" -Body @{
    studentCode = $studentCode
    schoolId = "SCH-001"
    grade = "8vo EGB"
    academicYear = "2026-2027"
    pendingAmount = 120.00
} | Out-Null

Invoke-CampusRequest -Method "POST" -Path "/api/payments" -Body @{
    paymentCode = $paymentCode
    studentCode = $studentCode
    description = "Matricula inicial"
    amount = 120.00
} | Out-Null

Invoke-CampusRequest -Method "POST" -Path "/api/payments/$paymentCode/confirm" -Body @{
    confirmationReference = "BANK-$suffix"
    confirmedAt = (Get-Date).ToUniversalTime().ToString("o")
} | Out-Null

Invoke-CampusRequest -Method "POST" -Path "/api/attendance/records" -Body @{
    studentCode = $studentCode
    classDate = (Get-Date).ToString("yyyy-MM-dd")
    type = "PRESENT"
    recordedBy = "Docente Demo"
    notes = "Asistencia creada por script de datos semilla"
} | Out-Null

Invoke-CampusRequest -Method "POST" -Path "/api/attendance/incidents" -Body @{
    studentCode = $studentCode
    severity = "MEDIUM"
    title = "Seguimiento academico"
    description = "Incidente demo para evidenciar flujo de bienestar"
    reportedBy = "Bienestar Demo"
    reportedAt = (Get-Date).ToUniversalTime().ToString("o")
} | Out-Null

$dashboard = Invoke-CampusRequest -Method "GET" -Path "/api/analytics/dashboard"
$events = Invoke-CampusRequest -Method "GET" -Path "/api/academic/students/$studentCode/events"

Write-Host "Demo data created."
Write-Host "Student: $studentCode"
Write-Host "Payment: $paymentCode"
Write-Host "Dashboard summary:"
$dashboard | ConvertTo-Json -Depth 8
Write-Host "Student event count: $($events.Count)"
