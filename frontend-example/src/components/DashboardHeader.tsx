import { useState } from 'react'
import { GraduationCapIcon, LogOutIcon, MenuIcon } from '../assets/icons'
import { DashboardView, User } from '../types'

interface DashboardHeaderProps {
  user: User
  onLogout: () => void
  activeView: DashboardView
  onNavigate: (view: DashboardView) => void
}

interface NavLink {
  label: string
  view: DashboardView
}

function getInitials(email: string): string {
  return email.slice(0, 2).toUpperCase()
}

export default function DashboardHeader({ user, onLogout, activeView, onNavigate }: DashboardHeaderProps) {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  const navLinks: NavLink[] = [
    { label: 'Catálogo', view: 'catalogo' },
    { label: 'Minhas Matérias', view: 'materias' },
    { label: 'Meu Perfil', view: 'perfil' },
  ]

  function handleNavigate(view: DashboardView) {
    onNavigate(view)
    setMobileMenuOpen(false)
  }

  return (
    <header className="bg-white border-b border-ui-border sticky top-0 z-10">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">

          {/* Logo */}
          <div className="flex items-center gap-2 shrink-0">
            <div className="bg-brand-light flex items-center justify-center w-8 h-8 rounded-lg">
              <GraduationCapIcon width={18} height={14} color="#3525cd" />
            </div>
            <span className="font-bold text-[17px] text-brand-primary tracking-tight hidden sm:block">
              MatriculaFácil
            </span>
          </div>

          <nav className="hidden md:flex justify-items-start gap-1">
            {navLinks.map((link) => (
              <button
                key={link.view}
                type="button"
                onClick={() => handleNavigate(link.view)}
                className={[
                  'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
                  activeView === link.view
                    ? 'bg-brand-light text-brand-primary'
                    : 'text-ui-medium hover:bg-ui-bg hover:text-ui-dark',
                ].join(' ')}
              >
                {link.label}
              </button>
            ))}
          </nav>

          <div className="flex items-center gap-3">

            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-full bg-brand-accent flex items-center justify-center shrink-0">
                <span className="text-white text-xs font-semibold leading-none">
                  {getInitials(user.email)}
                </span>
              </div>
              <span className="hidden sm:block text-sm font-medium text-ui-dark">{user.email}</span>
            </div>

            <button
              onClick={onLogout}
              className="hidden md:flex items-center gap-1.5 text-ui-muted hover:text-ui-dark transition-colors px-3 py-1.5 rounded-lg hover:bg-ui-bg text-sm font-medium"
            >
              <LogOutIcon />
              Sair
            </button>

            <button
              className="md:hidden text-ui-muted hover:text-ui-dark transition-colors p-1.5 rounded-lg hover:bg-ui-bg"
              onClick={() => setMobileMenuOpen((v) => !v)}
            >
              <MenuIcon />
            </button>
          </div>
        </div>

        {mobileMenuOpen && (
          <nav className="md:hidden border-t border-ui-border py-2 flex flex-col gap-1">
            {navLinks.map((link) => (
              <button
                key={link.view}
                type="button"
                onClick={() => handleNavigate(link.view)}
                className={[
                  'text-left px-4 py-2.5 rounded-lg text-sm font-medium transition-colors',
                  activeView === link.view
                    ? 'bg-brand-light text-brand-primary'
                    : 'text-ui-medium hover:bg-ui-bg',
                ].join(' ')}
              >
                {link.label}
              </button>
            ))}
            <button
              onClick={onLogout}
              className="flex items-center gap-1.5 px-4 py-2.5 rounded-lg text-sm font-medium text-ui-medium hover:bg-ui-bg transition-colors"
            >
              <LogOutIcon />
              Sair
            </button>
          </nav>
        )}
      </div>
    </header>
  )
}
