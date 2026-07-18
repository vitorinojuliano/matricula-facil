import { useEffect, useState } from 'react'
import api from '../services/api'
import { DisciplinaDetalhes } from '../types'

interface DisciplinaDetalhesModalProps {
  disciplinaId: number
  onClose: () => void
}

export default function DisciplinaDetalhesModal({ disciplinaId, onClose }: DisciplinaDetalhesModalProps) {
  const [detalhes, setDetalhes] = useState<DisciplinaDetalhes | null>(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
    api
      .get<DisciplinaDetalhes>(`/disciplina/${disciplinaId}`)
      .then((resposta) => setDetalhes(resposta.data))
      .catch(() => setErro('Não foi possível carregar os detalhes da disciplina.'))
      .finally(() => setCarregando(false))
  }, [disciplinaId])

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-20" onClick={onClose}>
      <div
        className="bg-white rounded-xl p-6 max-w-md w-full max-h-[85vh] overflow-y-auto flex flex-col gap-4"
        onClick={(e) => e.stopPropagation()}
      >
        {carregando && <p className="text-sm text-ui-muted">Carregando detalhes...</p>}

        {!carregando && erro && <p className="text-sm text-red-600">{erro}</p>}

        {!carregando && detalhes && (
          <>
            <div className="flex items-start justify-between gap-2">
              <div>
                <h2 className="font-bold text-lg text-ui-dark leading-6">{detalhes.nome}</h2>
                <p className="text-xs text-ui-muted mt-0.5">{detalhes.codigo}</p>
              </div>
              <button
                onClick={onClose}
                aria-label="Fechar"
                className="text-ui-muted hover:text-ui-dark text-xl leading-none"
              >
                ×
              </button>
            </div>

            {detalhes.descricao && <p className="text-sm text-ui-medium">{detalhes.descricao}</p>}

            <div className="flex flex-col gap-1 text-sm text-ui-medium">
              <span>Professor: {detalhes.professor}</span>
              <span>{detalhes.creditos} créditos</span>
              <span>{detalhes.vagas} vagas</span>
              <span>Horário: {detalhes.horario}</span>
            </div>

            {detalhes.preRequisitosDetalhados.length > 0 && (
              <div>
                <p className="text-xs font-semibold text-ui-dark mb-1">Pré-requisitos</p>
                <ul className="flex flex-col gap-1">
                  {detalhes.preRequisitosDetalhados.map((pr) => (
                    <li key={pr.codigo} className="text-xs text-ui-medium">
                      {pr.nome} ({pr.codigo}) —{' '}
                      <span className={pr.status === 'CURSADO' ? 'text-green-700 font-medium' : 'text-red-700 font-medium'}>
                        {pr.status === 'CURSADO' ? 'Cursado' : 'Pendente'}
                      </span>
                    </li>
                  ))}
                </ul>
              </div>
            )}

            <button
              onClick={onClose}
              className="mt-2 w-full text-sm font-medium px-4 py-2 rounded-lg border border-ui-border text-ui-medium hover:bg-ui-bg transition-colors"
            >
              Fechar
            </button>
          </>
        )}
      </div>
    </div>
  )
}
