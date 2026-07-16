export default function Stat({ label, value, tone = 'neutral' }) {
  return (
    <div className={`stat stat-${tone}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  )
}
