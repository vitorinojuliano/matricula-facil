import { useState } from 'react'
import { Matricula } from '../types'

interface MatriculaRowProps {
  matricula: Matricula
  onCancelar: (matriculaId: number) => Promise<void>
}

const STATUS_LABEL: Record<string, string> = {
  INSCRITO: 'Inscrito',
  CONCLUIDA: 'Concluída',
  REPROVADA: 'Reprovada',
  CANCELADA: 'Cancelada',
}

const STATUS_CLASS: Record<string, string> = {
  INSCRITO: 'bg-green-50 text-green-700 border-green-200',
  CONCLUIDA: 'bg-brand-light text-brand-primary border-brand-light',
  REPROVADA: 'bg-red-50 text-red-700 border-red-200',
  CANCELADA: 'bg-ui-bg text-ui-muted border-ui-border',
}

export default function MatriculaRow({ matricula, onCancelar }: MatriculaRowProps) {
  const { id, nomeDaMatricula, codigoDaDisciplina, creditos, horario, status } = matricula
  const podeCancelar = status === 'INSCRITO'

  const [cancelando, setCancelando] = useState(false)
  const [erro, setErro] = useState('')

  async function handleCancelar() {
    setCancelando(true)
    setErro('')
    try {
      await onCancelar(id)
    } catch (err: any) {
      setErro(err.response?.data?.message || 'Não foi possível cancelar.')
      setCancelando(false)
    }
  }

  return (
    <div className="bg-white border border-ui-border rounded-xl p-4 sm:p-5 flex flex-col sm:flex-row sm:items-center gap-3 sm:gap-4">
      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-2 flex-wrap">
          <h3 className="font-semibold text-base text-ui-dark leading-5">{nomeDaMatricula}</h3>
          <span
            className={`text-xs font-medium px-2.5 py-1 rounded-full border ${
              STATUS_CLASS[status] ?? STATUS_CLASS.CANCELADA
            }`}
          >
            {STATUS_LABEL[status] ?? status}
          </span>
        </div>
        <p className="text-xs text-ui-muted mt-1">
          {codigoDaDisciplina} · {creditos} créditos · {horario}
        </p>
        {erro && <p className="text-xs font-medium text-red-600 mt-1">{erro}</p>}
      </div>

      {podeCancelar && (
        <button
          onClick={handleCancelar}
          disabled={cancelando}
          className="shrink-0 text-sm font-medium px-4 py-2 rounded-lg border border-red-200 text-red-600 hover:bg-red-50 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {cancelando ? 'Cancelando...' : 'Cancelar'}
        </button>
      )}
    </div>
  )
}
