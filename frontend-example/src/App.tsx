import { useState } from 'react'
import LoginPage from './pages/LoginPage'
import SignupPage from './pages/SignupPage'
import DashboardPage from './pages/DashboardPage'
import { Page, User } from './types'

function getUsuarioSalvo(): User | null {
  const salvo = localStorage.getItem('user')
  return salvo ? JSON.parse(salvo) : null
}

export default function App() {
  const [user, setUser] = useState<User | null>(getUsuarioSalvo())
  const [page, setPage] = useState<Page>(user ? 'dashboard' : 'login')

  function handleLoginSuccess(usuarioLogado: User) {
    localStorage.setItem('user', JSON.stringify(usuarioLogado))
    setUser(usuarioLogado)
    setPage('dashboard')
  }

  function handleLogout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
    setPage('login')
  }

  function handleUserUpdate(usuarioAtualizado: User) {
    localStorage.setItem('user', JSON.stringify(usuarioAtualizado))
    setUser(usuarioAtualizado)
  }

  if (page === 'signup') return <SignupPage onNavigate={setPage} />
  if (page === 'dashboard' && user)
    return <DashboardPage user={user} onLogout={handleLogout} onUserUpdate={handleUserUpdate} />
  return <LoginPage onNavigate={setPage} onLoginSuccess={handleLoginSuccess} />
}
