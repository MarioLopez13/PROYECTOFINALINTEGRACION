import Field from '../../components/Field.jsx'
import Stat from '../../components/Stat.jsx'
import { dateTime } from '../../shared/formatters.js'

export default function AcademicView({
  createDemoDay,
  loading,
  selectedStudent,
  selectedStudentDetail,
  setSelectedStudent,
  studentEvents,
  studentForm,
  students,
  submitStudent,
  updateStudentForm,
}) {
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
                  <small>{dateTime(event.occurredAt)} Â· {event.correlationId}</small>
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
