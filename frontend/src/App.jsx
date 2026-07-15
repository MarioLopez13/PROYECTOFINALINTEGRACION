import { Route, Routes } from 'react-router-dom'

function Home() {
  return (
    <main>
      <h1>CampusConnect360</h1>
      <p>Base técnica del frontend preparada.</p>
    </main>
  )
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
    </Routes>
  )
}
