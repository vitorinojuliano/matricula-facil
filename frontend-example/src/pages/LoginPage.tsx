import LoginCard from '../components/LoginCard'
import { Page, User } from '../types'

interface LoginPageProps {
  onNavigate?: (page: Page) => void
  onLoginSuccess: (user: User) => void
}

export default function LoginPage({ onNavigate, onLoginSuccess }: LoginPageProps) {
  return (
    <div className="min-h-screen w-full bg-ui-bg flex items-center justify-center px-4 py-8 sm:py-16">
      <div className="w-full max-w-[420px]">
        <LoginCard onNavigate={onNavigate} onLoginSuccess={onLoginSuccess} />
      </div>
    </div>
  )
}