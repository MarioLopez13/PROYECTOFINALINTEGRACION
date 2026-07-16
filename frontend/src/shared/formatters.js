export function money(value) {
  if (value === null || value === undefined || value === '') return '$0.00'
  return `$${Number(value).toFixed(2)}`
}

export function dateTime(value) {
  if (!value) return 'Sin fecha'
  return new Intl.DateTimeFormat('es-EC', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(new Date(value))
}
