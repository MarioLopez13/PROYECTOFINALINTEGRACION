export const tabs = [
  { id: 'academic', label: 'Secretaria' },
  { id: 'payments', label: 'Finanzas' },
  { id: 'wellbeing', label: 'Bienestar' },
  { id: 'dashboard', label: 'Dashboard' },
]

export const today = new Date().toISOString().slice(0, 10)

export const initialStudentForm = {
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

export const initialPaymentForm = {
  paymentCode: '',
  studentCode: '',
  description: 'Pension inicial',
  amount: '120.00',
}

export const initialAttendanceForm = {
  studentCode: '',
  classDate: today,
  type: 'PRESENT',
  recordedBy: 'Docente Demo',
  notes: '',
}

export const initialIncidentForm = {
  studentCode: '',
  severity: 'MEDIUM',
  title: '',
  description: '',
  reportedBy: 'Bienestar Demo',
}
