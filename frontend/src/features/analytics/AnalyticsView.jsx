import Stat from '../../components/Stat.jsx'
import { dateTime } from '../../shared/formatters.js'

export default function AnalyticsView({ analyticsEvents, dashboard, loading, refreshAll }) {
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
              <small>{event.correlationId} Â· {dateTime(event.processedAt)}</small>
            </article>
          ))}
          {!analyticsEvents.length && <p className="empty">Aun no hay eventos procesados.</p>}
        </div>
      </section>
    </section>
  )
}
