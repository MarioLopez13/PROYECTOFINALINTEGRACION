import Field from '../../components/Field.jsx'
import { dateTime } from '../../shared/formatters.js'

export default function AttendanceView({
  attendanceForm,
  attendanceRecords,
  incidentForm,
  incidents,
  loading,
  notifications,
  selectedStudent,
  simulateNotificationFailure,
  submitAttendance,
  submitIncident,
  updateAttendanceForm,
  updateIncidentForm,
}) {
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
                <span>{notification.studentCode} Â· {notification.recipient}</span>
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
                <span>{record.studentCode} Â· {record.recordedBy}</span>
              </div>
              <small>{record.classDate}</small>
            </article>
          ))}
          {incidents.slice(0, 6).map((incident) => (
            <article key={`incident-${incident.id}`} className="row status-failed">
              <div>
                <strong>{incident.title}</strong>
                <span>{incident.studentCode} Â· {incident.severity}</span>
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
