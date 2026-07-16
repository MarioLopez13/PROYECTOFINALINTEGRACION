import { useCallback, useEffect, useMemo, useState } from 'react'
import AcademicView from './features/academic/AcademicView.jsx'
import AnalyticsView from './features/analytics/AnalyticsView.jsx'
import AttendanceView from './features/attendance/AttendanceView.jsx'
import PaymentsView from './features/payments/PaymentsView.jsx'
import api from './services/api.js'
import {
  initialAttendanceForm,
  initialIncidentForm,
  initialPaymentForm,
  initialStudentForm,
  tabs,
  today,
} from './shared/constants.js'

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

  const views = {
    academic: (
      <AcademicView
        createDemoDay={createDemoDay}
        loading={loading}
        selectedStudent={selectedStudent}
        selectedStudentDetail={selectedStudentDetail}
        setSelectedStudent={setSelectedStudent}
        studentEvents={studentEvents}
        studentForm={studentForm}
        students={students}
        submitStudent={submitStudent}
        updateStudentForm={updateStudentForm}
      />
    ),
    payments: (
      <PaymentsView
        confirmedPayments={confirmedPayments}
        confirmPayment={confirmPayment}
        loading={loading}
        paymentForm={paymentForm}
        pendingPayments={pendingPayments}
        selectedStudent={selectedStudent}
        submitPayment={submitPayment}
        updatePaymentForm={updatePaymentForm}
      />
    ),
    wellbeing: (
      <AttendanceView
        attendanceForm={attendanceForm}
        attendanceRecords={attendanceRecords}
        incidentForm={incidentForm}
        incidents={incidents}
        loading={loading}
        notifications={notifications}
        selectedStudent={selectedStudent}
        simulateNotificationFailure={simulateNotificationFailure}
        submitAttendance={submitAttendance}
        submitIncident={submitIncident}
        updateAttendanceForm={updateAttendanceForm}
        updateIncidentForm={updateIncidentForm}
      />
    ),
    dashboard: (
      <AnalyticsView
        analyticsEvents={analyticsEvents}
        dashboard={dashboard}
        loading={loading}
        refreshAll={refreshAll}
      />
    ),
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

      {views[activeTab]}
    </main>
  )
}
