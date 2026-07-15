import { useCallback, useEffect, useMemo, useState } from 'react'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const API_KEY = import.meta.env.VITE_API_KEY || 'campus-demo-key'

const tabs = [
  { id: 'academic', label: 'Secretaria' },
  { id: 'payments', label: 'Finanzas' },
  { id: 'wellbeing', label: 'Bienestar' },
  { id: 'dashboard', label: 'Dashboard' },
]

const today = new Date().toISOString().slice(0, 10)

const initialStudentForm = {
  studentCode: '',
  documentNumber: '',
  firstName: '',
  lastName: '',
  birthDate: '2012-05-10',
  representativeEmail: '',
  representativePhone: '',
  schoolId: 'SCH-001',
  grade: '8vo EGB',
  academicYear: '2026-2027',
  pendingAmount: '120.00',
}

const initialPaymentForm = {
  paymentCode: '',
  studentCode: '',
  description: 'Pension inicial',
  amount: '120.00',
}

const initialAttendanceForm = {
  studentCode: '',
  classDate: today,
  type: 'PRESENT',
  recordedBy: 'Docente Demo',
  notes: '',
}

const initialIncidentForm = {
  studentCode: '',
  severity: 'MEDIUM',
  title: '',
  description: '',
  reportedBy: 'Bienestar Demo',
}

async function api(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      'X-API-Key': API_KEY,
      ...(options.headers || {}),
    },
    body: options.body ? JSON.stringify(options.body) : undefined,
  })

  const text = await response.text()
  const data = text ? JSON.parse(text) : null

  if (!response.ok) {
    throw new Error(data?.message || `Request failed with status ${response.status}`)
  }

  return data
}

function money(value) {
  if (value === null || value === undefined || value === '') return '$0.00'
  return `$${Number(value).toFixed(2)}`
}

function dateTime(value) {
  if (!value) return 'Sin fecha'
  return new Intl.DateTimeFormat('es-EC', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(new Date(value))
}

function Field({ label, children }) {
  return (
    <label className="field">
      <span>{label}</span>
      {children}
    </label>
  )
}

function Stat({ label, value, tone = 'neutral' }) {
  return (
    <div className={`stat stat-${tone}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  )
}

export default function App() {
  const [activeTab, setActiveTab] = useState('academic')
  const [students, setStudents] = useState([])
  const [pendingPayments, setPendingPayments] = useState([])
  const [confirmedPayments, setConfirmedPayments] = useState([])
  const [attendanceRecords, setAttendanceRecords] = useState([])
  const [incidents, setIncidents] = useState([])
  const [notifications, setNotifications] = useState([])
  const [dashboard, setDashboard] = useState(null)
  const [analyticsEvents, setAnalyticsEvents] = useState([])
  const [studentEvents, setStudentEvents] = useState([])
  const [selectedStudent, setSelectedStudent] = useState('')
  const [studentForm, setStudentForm] = useState(initialStudentForm)
  const [paymentForm, setPaymentForm] = useState(initialPaymentForm)
  const [attendanceForm, setAttendanceForm] = useState(initialAttendanceForm)
  const [incidentForm, setIncidentForm] = useState(initialIncidentForm)
  const [notice, setNotice] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const selectedStudentDetail = useMemo(
    () => students.find((student) => student.studentCode === selectedStudent),
    [selectedStudent, students],
  )

  async function run(action, successMessage) {
    setLoading(true)
    setError('')
    try {
      await action()
      setNotice(successMessage)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const refreshAll = useCallback(async () => {
    const [
      studentsResult,
      pendingResult,
      confirmedResult,
      attendanceResult,
      incidentsResult,
      notificationsResult,
      dashboardResult,
      eventsResult,
    ] = await Promise.allSettled([
      api('/api/academic/students'),
      api('/api/payments?status=PENDING'),
      api('/api/payments?status=CONFIRMED'),
      api('/api/attendance/records'),
      api('/api/attendance/incidents'),
      api('/api/notifications'),
      api('/api/analytics/dashboard'),
      api('/api/analytics/events'),
    ])

    if (studentsResult.status === 'fulfilled') {
      setStudents(studentsResult.value || [])
      setSelectedStudent((current) => current || studentsResult.value?.[0]?.studentCode || '')
    }
    if (pendingResult.status === 'fulfilled') setPendingPayments(pendingResult.value || [])
    if (confirmedResult.status === 'fulfilled') setConfirmedPayments(confirmedResult.value || [])
    if (attendanceResult.status === 'fulfilled') setAttendanceRecords(attendanceResult.value || [])
    if (incidentsResult.status === 'fulfilled') setIncidents(incidentsResult.value || [])
    if (notificationsResult.status === 'fulfilled') setNotifications(notificationsResult.value || [])
    if (dashboardResult.status === 'fulfilled') setDashboard(dashboardResult.value)
    if (eventsResult.status === 'fulfilled') setAnalyticsEvents(eventsResult.value || [])
  }, [])

  const refreshStudentEvents = useCallback(async (studentCode) => {
    if (!studentCode) {
      setStudentEvents([])
      return
    }
    setStudentEvents(await api(`/api/academic/students/${studentCode}/events`))
  }, [])

  useEffect(() => {
    refreshAll().catch((err) => setError(err.message))
  }, [refreshAll])

  useEffect(() => {
    refreshStudentEvents(selectedStudent).catch(() => setStudentEvents([]))
  }, [refreshStudentEvents, selectedStudent])

  function updateStudentForm(field, value) {
    setStudentForm((current) => ({ ...current, [field]: value }))
  }

  function updatePaymentForm(field, value) {
    setPaymentForm((current) => ({ ...current, [field]: value }))
  }

  function updateAttendanceForm(field, value) {
    setAttendanceForm((current) => ({ ...current, [field]: value }))
  }

  function updateIncidentForm(field, value) {
    setIncidentForm((current) => ({ ...current, [field]: value }))
  }

  async function submitStudent(event) {
    event.preventDefault()
    await run(async () => {
      await api('/api/academic/students', {
        method: 'POST',
        body: {
          studentCode: studentForm.studentCode,
          documentNumber: studentForm.documentNumber,
          firstName: studentForm.firstName,
          lastName: studentForm.lastName,
          birthDate: studentForm.birthDate,
          representativeEmail: studentForm.representativeEmail,
          representativePhone: studentForm.representativePhone,
        },
      })
      await api('/api/academic/enrollments', {
        method: 'POST',
        body: {
          studentCode: studentForm.studentCode,
          schoolId: studentForm.schoolId,
          grade: studentForm.grade,
          academicYear: studentForm.academicYear,
          pendingAmount: Number(studentForm.pendingAmount),
        },
      })
      setSelectedStudent(studentForm.studentCode)
      setPaymentForm((current) => ({ ...current, studentCode: studentForm.studentCode }))
      setAttendanceForm((current) => ({ ...current, studentCode: studentForm.studentCode }))
      setIncidentForm((current) => ({ ...current, studentCode: studentForm.studentCode }))
      await refreshAll()
      await refreshStudentEvents(studentForm.studentCode)
    }, 'Estudiante matriculado y evento StudentEnrolled publicado')
  }

  async function submitPayment(event) {
    event.preventDefault()
    await run(async () => {
      await api('/api/payments', {
        method: 'POST',
        body: {
          ...paymentForm,
          studentCode: paymentForm.studentCode || selectedStudent,
          amount: Number(paymentForm.amount),
        },
      })
      await refreshAll()
    }, 'Obligacion de pago creada')
  }

  async function confirmPayment(paymentCode) {
    await run(async () => {
      await api(`/api/payments/${paymentCode}/confirm`, {
        method: 'POST',
        body: {
          confirmationReference: `REF-${Date.now()}`,
          confirmedAt: new Date().toISOString(),
        },
      })
      await refreshAll()
      await refreshStudentEvents(selectedStudent)
    }, 'Pago confirmado y evento PaymentConfirmed publicado')
  }

  async function submitAttendance(event) {
    event.preventDefault()
    await run(async () => {
      await api('/api/attendance/records', {
        method: 'POST',
        body: {
          ...attendanceForm,
          studentCode: attendanceForm.studentCode || selectedStudent,
        },
      })
      await refreshAll()
    }, 'Asistencia registrada')
  }

  async function submitIncident(event) {
    event.preventDefault()
    await run(async () => {
      await api('/api/attendance/incidents', {
        method: 'POST',
        body: {
          ...incidentForm,
          studentCode: incidentForm.studentCode || selectedStudent,
          reportedAt: new Date().toISOString(),
        },
      })
      await refreshAll()
    }, 'Incidente reportado')
  }

  async function simulateNotificationFailure() {
    await run(async () => {
      await api('/api/notifications/simulate-failure', { method: 'POST' })
      await refreshAll()
    }, 'La siguiente notificacion fallara y pasara por DLQ')
  }

  async function createDemoDay() {
    await run(async () => {
      const suffix = String(Date.now()).slice(-5)
      const studentCode = `STU-${suffix}`
      await api('/api/academic/students', {
        method: 'POST',
        body: {
          studentCode,
          documentNumber: `DOC-${suffix}`,
          firstName: 'Camila',
          lastName: 'Rivera',
          birthDate: '2012-05-10',
          representativeEmail: `familia${suffix}@demo.edu`,
          representativePhone: '0999999999',
        },
      })
      await api('/api/academic/enrollments', {
        method: 'POST',
        body: {
          studentCode,
          schoolId: 'SCH-001',
          grade: '8vo EGB',
          academicYear: '2026-2027',
          pendingAmount: 120,
        },
      })
      await api('/api/payments', {
        method: 'POST',
        body: {
          paymentCode: `PAY-${suffix}`,
          studentCode,
          description: 'Matricula inicial',
          amount: 120,
        },
      })
      await api(`/api/payments/PAY-${suffix}/confirm`, {
        method: 'POST',
        body: {
          confirmationReference: `DEMO-${suffix}`,
          confirmedAt: new Date().toISOString(),
        },
      })
      await api('/api/attendance/records', {
        method: 'POST',
        body: {
          studentCode,
          classDate: today,
          type: 'PRESENT',
          recordedBy: 'Docente Demo',
          notes: 'Operacion diaria demo',
        },
      })
      setSelectedStudent(studentCode)
      await refreshAll()
      await refreshStudentEvents(studentCode)
    }, 'Demo completa creada')
  }

  const renderTab = () => {
    if (activeTab === 'academic') {
      return (
        <section className="workspace">
          <form className="panel form-panel" onSubmit={submitStudent}>
            <div className="panel-heading">
              <h2>Portal Academico</h2>
              <button type="button" className="secondary" onClick={createDemoDay} disabled={loading}>
                Demo rapida
              </button>
            </div>
            <div className="form-grid">
              <Field label="Codigo">
                <input value={studentForm.studentCode} onChange={(e) => updateStudentForm('studentCode', e.target.value)} required />
              </Field>
              <Field label="Documento">
                <input value={studentForm.documentNumber} onChange={(e) => updateStudentForm('documentNumber', e.target.value)} required />
              </Field>
              <Field label="Nombres">
                <input value={studentForm.firstName} onChange={(e) => updateStudentForm('firstName', e.target.value)} required />
              </Field>
              <Field label="Apellidos">
                <input value={studentForm.lastName} onChange={(e) => updateStudentForm('lastName', e.target.value)} required />
              </Field>
              <Field label="Nacimiento">
                <input type="date" value={studentForm.birthDate} onChange={(e) => updateStudentForm('birthDate', e.target.value)} required />
              </Field>
              <Field label="Email representante">
                <input type="email" value={studentForm.representativeEmail} onChange={(e) => updateStudentForm('representativeEmail', e.target.value)} required />
              </Field>
              <Field label="Telefono">
                <input value={studentForm.representativePhone} onChange={(e) => updateStudentForm('representativePhone', e.target.value)} required />
              </Field>
              <Field label="Colegio">
                <input value={studentForm.schoolId} onChange={(e) => updateStudentForm('schoolId', e.target.value)} required />
              </Field>
              <Field label="Grado">
                <input value={studentForm.grade} onChange={(e) => updateStudentForm('grade', e.target.value)} required />
              </Field>
              <Field label="Periodo">
                <input value={studentForm.academicYear} onChange={(e) => updateStudentForm('academicYear', e.target.value)} required />
              </Field>
              <Field label="Valor pendiente">
                <input type="number" min="0" step="0.01" value={studentForm.pendingAmount} onChange={(e) => updateStudentForm('pendingAmount', e.target.value)} required />
              </Field>
            </div>
            <button type="submit" disabled={loading}>Matricular estudiante</button>
          </form>

          <section className="panel">
            <div className="panel-heading">
              <h2>Ficha del estudiante</h2>
              <select value={selectedStudent} onChange={(e) => setSelectedStudent(e.target.value)}>
                <option value="">Seleccionar</option>
                {students.map((student) => (
                  <option key={student.studentCode} value={student.studentCode}>
                    {student.studentCode} - {student.fullName}
                  </option>
                ))}
              </select>
            </div>
            {selectedStudentDetail ? (
              <>
                <div className="profile-grid">
                  <Stat label="Estado academico" value={selectedStudentDetail.status} tone="good" />
                  <Stat label="Estado financiero" value={selectedStudentDetail.financialStatus} tone={selectedStudentDetail.financialStatus === 'PAID' ? 'good' : 'warn'} />
                  <Stat label="Representante" value={selectedStudentDetail.representativeEmail} />
                </div>
                <div className="timeline">
                  {studentEvents.map((event) => (
                    <article key={event.sourceEventId} className="timeline-item">
                      <strong>{event.eventType}</strong>
                      <span>{event.details}</span>
                      <small>{dateTime(event.occurredAt)} · {event.correlationId}</small>
                    </article>
                  ))}
                  {!studentEvents.length && <p className="empty">Sin eventos registrados para este estudiante.</p>}
                </div>
              </>
            ) : (
              <p className="empty">Registra o selecciona un estudiante.</p>
            )}
          </section>
        </section>
      )
    }

    if (activeTab === 'payments') {
      return (
        <section className="workspace">
          <form className="panel form-panel" onSubmit={submitPayment}>
            <div className="panel-heading">
              <h2>Portal Financiero</h2>
            </div>
            <div className="form-grid">
              <Field label="Codigo pago">
                <input value={paymentForm.paymentCode} onChange={(e) => updatePaymentForm('paymentCode', e.target.value)} required />
              </Field>
              <Field label="Estudiante">
                <input value={paymentForm.studentCode || selectedStudent} onChange={(e) => updatePaymentForm('studentCode', e.target.value)} required />
              </Field>
              <Field label="Descripcion">
                <input value={paymentForm.description} onChange={(e) => updatePaymentForm('description', e.target.value)} required />
              </Field>
              <Field label="Monto">
                <input type="number" min="0.01" step="0.01" value={paymentForm.amount} onChange={(e) => updatePaymentForm('amount', e.target.value)} required />
              </Field>
            </div>
            <button type="submit" disabled={loading}>Registrar deuda</button>
          </form>

          <section className="panel">
            <div className="panel-heading">
              <h2>Pagos pendientes</h2>
              <span className="counter">{pendingPayments.length}</span>
            </div>
            <div className="list">
              {pendingPayments.map((payment) => (
                <article key={payment.paymentCode} className="row">
                  <div>
                    <strong>{payment.paymentCode}</strong>
                    <span>{payment.studentCode} · {payment.description}</span>
                  </div>
                  <div className="row-actions">
                    <strong>{money(payment.amount)}</strong>
                    <button type="button" onClick={() => confirmPayment(payment.paymentCode)} disabled={loading}>Confirmar</button>
                  </div>
                </article>
              ))}
              {!pendingPayments.length && <p className="empty">No hay pagos pendientes.</p>}
            </div>
          </section>

          <section className="panel">
            <div className="panel-heading">
              <h2>Pagos confirmados</h2>
              <span className="counter">{confirmedPayments.length}</span>
            </div>
            <div className="list">
              {confirmedPayments.map((payment) => (
                <article key={payment.paymentCode} className="row">
                  <div>
                    <strong>{payment.paymentCode}</strong>
                    <span>{payment.studentCode} · {payment.confirmationReference}</span>
                  </div>
                  <small>{dateTime(payment.confirmedAt)}</small>
                </article>
              ))}
            </div>
          </section>
        </section>
      )
    }

    if (activeTab === 'wellbeing') {
      return (
        <section className="workspace">
          <form className="panel form-panel" onSubmit={submitAttendance}>
            <div className="panel-heading">
              <h2>Asistencia</h2>
            </div>
            <div className="form-grid">
              <Field label="Estudiante">
                <input value={attendanceForm.studentCode || selectedStudent} onChange={(e) => updateAttendanceForm('studentCode', e.target.value)} required />
              </Field>
              <Field label="Fecha">
                <input type="date" value={attendanceForm.classDate} onChange={(e) => updateAttendanceForm('classDate', e.target.value)} required />
              </Field>
              <Field label="Tipo">
                <select value={attendanceForm.type} onChange={(e) => updateAttendanceForm('type', e.target.value)}>
                  <option value="PRESENT">Presente</option>
                  <option value="ABSENT">Ausente</option>
                  <option value="LATE">Atraso</option>
                </select>
              </Field>
              <Field label="Registrado por">
                <input value={attendanceForm.recordedBy} onChange={(e) => updateAttendanceForm('recordedBy', e.target.value)} required />
              </Field>
              <Field label="Notas">
                <input value={attendanceForm.notes} onChange={(e) => updateAttendanceForm('notes', e.target.value)} />
              </Field>
            </div>
            <button type="submit" disabled={loading}>Registrar asistencia</button>
          </form>

          <form className="panel form-panel" onSubmit={submitIncident}>
            <div className="panel-heading">
              <h2>Incidente o novedad</h2>
              <button type="button" className="secondary danger" onClick={simulateNotificationFailure} disabled={loading}>
                Fallar siguiente notificacion
              </button>
            </div>
            <div className="form-grid">
              <Field label="Estudiante">
                <input value={incidentForm.studentCode || selectedStudent} onChange={(e) => updateIncidentForm('studentCode', e.target.value)} required />
              </Field>
              <Field label="Severidad">
                <select value={incidentForm.severity} onChange={(e) => updateIncidentForm('severity', e.target.value)}>
                  <option value="LOW">Baja</option>
                  <option value="MEDIUM">Media</option>
                  <option value="HIGH">Alta</option>
                </select>
              </Field>
              <Field label="Titulo">
                <input value={incidentForm.title} onChange={(e) => updateIncidentForm('title', e.target.value)} required />
              </Field>
              <Field label="Reportado por">
                <input value={incidentForm.reportedBy} onChange={(e) => updateIncidentForm('reportedBy', e.target.value)} required />
              </Field>
              <Field label="Descripcion">
                <textarea value={incidentForm.description} onChange={(e) => updateIncidentForm('description', e.target.value)} required />
              </Field>
            </div>
            <button type="submit" disabled={loading}>Reportar incidente</button>
          </form>

          <section className="panel">
            <div className="panel-heading">
              <h2>Notificaciones</h2>
              <span className="counter">{notifications.length}</span>
            </div>
            <div className="list compact">
              {notifications.map((notification) => (
                <article key={notification.id || notification.sourceEventId} className={`row status-${notification.status.toLowerCase()}`}>
                  <div>
                    <strong>{notification.subject}</strong>
                    <span>{notification.studentCode} · {notification.recipient}</span>
                  </div>
                  <small>{notification.status}</small>
                </article>
              ))}
            </div>
          </section>

          <section className="panel">
            <div className="panel-heading">
              <h2>Registros recientes</h2>
              <span className="counter">{attendanceRecords.length + incidents.length}</span>
            </div>
            <div className="list compact">
              {attendanceRecords.slice(0, 6).map((record) => (
                <article key={`attendance-${record.id}`} className="row">
                  <div>
                    <strong>{record.type}</strong>
                    <span>{record.studentCode} · {record.recordedBy}</span>
                  </div>
                  <small>{record.classDate}</small>
                </article>
              ))}
              {incidents.slice(0, 6).map((incident) => (
                <article key={`incident-${incident.id}`} className="row status-failed">
                  <div>
                    <strong>{incident.title}</strong>
                    <span>{incident.studentCode} · {incident.severity}</span>
                  </div>
                  <small>{dateTime(incident.reportedAt)}</small>
                </article>
              ))}
              {!attendanceRecords.length && !incidents.length && <p className="empty">Sin registros de bienestar.</p>}
            </div>
          </section>
        </section>
      )
    }

    return (
      <section className="dashboard-layout">
        <section className="metrics">
          <Stat label="Matriculados" value={dashboard?.enrolledStudents ?? 0} tone="good" />
          <Stat label="Pagos confirmados" value={dashboard?.paymentsConfirmed ?? 0} tone="good" />
          <Stat label="Pagos pendientes" value={dashboard?.paymentsPending ?? 0} tone="warn" />
          <Stat label="Asistencias" value={dashboard?.attendanceRecords ?? 0} />
          <Stat label="Incidentes" value={dashboard?.incidentsReported ?? 0} tone="danger" />
          <Stat label="Eventos procesados" value={dashboard?.eventsProcessed ?? 0} />
          <Stat label="Mensajes fallidos" value={dashboard?.failedMessages ?? 0} tone="danger" />
          <Stat label="Estado ecosistema" value={dashboard?.ecosystemStatus ?? 'WAITING'} tone={dashboard?.ecosystemStatus === 'HEALTHY' ? 'good' : 'warn'} />
        </section>

        <section className="panel">
          <div className="panel-heading">
            <h2>Trazabilidad de eventos</h2>
            <button type="button" className="secondary" onClick={refreshAll} disabled={loading}>Actualizar</button>
          </div>
          <div className="timeline">
            {analyticsEvents.map((event) => (
              <article key={event.sourceEventId} className="timeline-item">
                <strong>{event.eventType}</strong>
                <span>{event.summary}</span>
                <small>{event.correlationId} · {dateTime(event.processedAt)}</small>
              </article>
            ))}
            {!analyticsEvents.length && <p className="empty">Aun no hay eventos procesados.</p>}
          </div>
        </section>
      </section>
    )
  }

  return (
    <main className="app-shell">
      <header className="topbar">
        <div>
          <span className="eyebrow">CampusConnect 360</span>
          <h1>Operacion diaria de colegios</h1>
        </div>
        <button type="button" className="secondary" onClick={refreshAll} disabled={loading}>
          Sincronizar
        </button>
      </header>

      <nav className="tabs" aria-label="Modulos">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            type="button"
            className={activeTab === tab.id ? 'active' : ''}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </nav>

      {(notice || error) && (
        <div className={`notice ${error ? 'error' : ''}`}>
          {error || notice}
        </div>
      )}

      {renderTab()}
    </main>
  )
}
