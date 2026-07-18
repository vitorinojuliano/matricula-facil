import { FormEvent, useState } from 'react'
import { GraduationCapIcon, EmailIcon, LockIcon, ArrowRightIcon } from '../assets/icons'
import InputField from './InputField'
import { Page, User } from '../types'
import api from '../services/api'

interface LoginCardProps {
  onNavigate?: (page: Page) => void
  onLoginSuccess: (user: User) => void
}

export default function LoginCard({ onNavigate, onLoginSuccess }: LoginCardProps) {
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(false)

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setErro('')
    setCarregando(true)

    try {
      const resposta = await api.post('/login', { email, senha })
      localStorage.setItem('token', resposta.data.token)
      onLoginSuccess(resposta.data.user)
    } catch (err: any) {
      setErro(err.response?.data?.message || 'Não foi possível entrar. Tente novamente.')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="bg-white border border-ui-border rounded-xl drop-shadow-[0px_1px_1px_rgba(0,0,0,0.05)] flex flex-col gap-8 p-6 sm:p-[33px]">
      <div className="flex flex-col items-center gap-1 w-full">
        <div className="bg-brand-light flex items-center justify-center w-12 py-[10px] rounded-xl">
          <GraduationCapIcon />
        </div>

        <div className="flex flex-col items-center w-full pt-3">
          <h1 className="font-bold text-[30px] text-brand-primary tracking-[-0.6px] leading-[38px] text-center w-full">
            MatriculaFácil
          </h1>
          <p className="text-base text-ui-medium leading-6 text-center">
            Acesse o Portal do Aluno
          </p>
        </div>
      </div>

      <form className="flex flex-col gap-6 w-full" onSubmit={handleSubmit}>
        <InputField
          label="E-mail"
          icon={<EmailIcon />}
          type="email"
          placeholder="seu@email.com"
          name="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <InputField
          label="Senha"
          icon={<LockIcon />}
          type="password"
          placeholder="••••••••"
          name="senha"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
        />

        {erro && (
          <p className="text-sm text-red-600">{erro}</p>
        )}

        <div className="pt-2">
          <button
            type="submit"
            disabled={carregando}
            className="w-full flex items-center justify-center gap-2 bg-brand-primary text-white font-medium text-sm leading-5 px-4 py-2 rounded-lg hover:bg-indigo-700 active:bg-indigo-800 transition-colors disabled:opacity-60"
          >
            {carregando ? 'Entrando...' : 'Entrar'}
            {!carregando && <ArrowRightIcon />}
          </button>
        </div>
      </form>

      <div className="border-t border-ui-border w-full pt-[25px]">
        <div className="flex items-center justify-center gap-1">
          <span className="text-base text-ui-medium leading-6">
            Não tem uma conta?
          </span>
          <button
            type="button"
            onClick={() => onNavigate?.('signup')}
            className="font-medium text-sm text-brand-primary leading-5 hover:underline"
          >
            Cadastre-se
          </button>
        </div>
      </div>
    </div>
  )
}