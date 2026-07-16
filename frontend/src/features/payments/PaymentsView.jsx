import Field from '../../components/Field.jsx'
import { dateTime, money } from '../../shared/formatters.js'

export default function PaymentsView({
  confirmedPayments,
  confirmPayment,
  loading,
  paymentForm,
  pendingPayments,
  selectedStudent,
  submitPayment,
  updatePaymentForm,
}) {
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
                <span>{payment.studentCode} Â· {payment.description}</span>
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
                <span>{payment.studentCode} Â· {payment.confirmationReference}</span>
              </div>
              <small>{dateTime(payment.confirmedAt)}</small>
            </article>
          ))}
        </div>
      </section>
    </section>
  )
}
