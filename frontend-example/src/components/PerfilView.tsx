import { FormEvent, useEffect, useState } from 'react'
import InputField from './InputField'
import api from '../services/api'
import { AtualizacaoPerfilRequest, User } from '../types'

interface PerfilViewProps {
  onEmailAtualizado: (novoEmail: string) => void
}

export default function PerfilView({ onEmailAtualizado }: PerfilViewProps) {
  const [carregando, setCarregando] = useState(true)
  const [erroCarregar, setErroCarregar] = useState('')

  const [email, setEmail] = useState('')
  const [senhaAtual, setSenhaAtual] = useState('')
  const [novaSenha, setNovaSenha] = useState('')

  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState(false)

  useEffect(() => {
    api
      .get<User>('/user/me')
      .then((resposta) => setEmail(resposta.data.email))
      .catch(() => setErroCarregar('Não foi possível carregar seu perfil.'))
      .finally(() => setCarregando(false))
  }, [])

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setErro('')
    setSucesso(false)
    setSalvando(true)

    const corpo: AtualizacaoPerfilRequest = { email }
    if (novaSenha) {
      corpo.senhaAtual = senhaAtual
      corpo.novaSenha = novaSenha
    }

    try {
      await api.put('/user/me', corpo)
      setSucesso(true)
      setSenhaAtual('')
      setNovaSenha('')
      onEmailAtualizado(email)
    } catch (err: any) {
      setErro(err.response?.data?.message || 'Não foi possível atualizar o perfil.')
    } finally {
      setSalvando(false)
    }
  }

  if (carregando) return <p className="mt-8 text-sm text-ui-muted">Carregando perfil...</p>
  if (erroCarregar) return <p className="mt-8 text-sm text-red-600">{erroCarregar}</p>

  return (
    <div className="mt-8 max-w-md">
      <form onSubmit={handleSubmit} className="bg-white border border-ui-border rounded-xl p-6 flex flex-col gap-5">
        <InputField label="E-mail" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />

        <div className="border-t border-ui-border pt-4 flex flex-col gap-4">
          <p className="text-sm font-medium text-ui-dark">Trocar senha (opcional)</p>

          <InputField
            label="Senha atual"
            type="password"
            value={senhaAtual}
            onChange={(e) => setSenhaAtual(e.target.value)}
          />

          <InputField label="Nova senha" type="password" value={novaSenha} onChange={(e) => setNovaSenha(e.target.value)} />
        </div>

        {erro && <p className="text-xs font-medium text-red-600">{erro}</p>}
        {sucesso && <p className="text-xs font-medium text-green-700">Perfil atualizado com sucesso.</p>}

        <button
          type="submit"
          disabled={salvando}
          className="w-full text-sm font-medium px-4 py-2 rounded-lg bg-brand-primary text-white hover:bg-indigo-700 active:bg-indigo-800 transition-colors disabled:opacity-50"
        >
          {salvando ? 'Salvando...' : 'Salvar alterações'}
        </button>
      </form>
    </div>
  )
}
