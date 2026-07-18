import { useState } from 'react'
import { Disciplina, Matricula } from '../types'
import DisciplinaDetalhesModal from './DisciplinaDetalhesModal'

interface DisciplinaCardProps {
  disciplina: Disciplina
  creditosAtuais: number
  matriculasInscritas: Matricula[]
  onInscrever: (disciplinaId: number) => Promise<void>
}

const LIMITE_CREDITOS = 24

export default function DisciplinaCard({
  disciplina,
  creditosAtuais,
  matriculasInscritas,
  onInscrever,
}: DisciplinaCardProps) {
  const {
    id,
    nome,
    codigo,
    creditos,
    vagas,
    horario,
    status,
    preRequisitoCodigo,
    preRequisitoStatus,
    matriculaStatus,
  } = disciplina
  const preRequisitoAtendido = !preRequisitoCodigo || preRequisitoStatus === 'ATENDIDO'
  const disponivelParaInscricao = matriculaStatus === null && status === 'DISPONIVEL'

  const conflitoHorario = matriculasInscritas.find((m) => m.horario === horario)
  const excedeCreditos = creditosAtuais + creditos > LIMITE_CREDITOS


  let motivoBloqueio: { cor: 'vermelho' | 'laranja'; mensagem: string } | null = null
  if (disponivelParaInscricao) {
    if (!preRequisitoAtendido) {
      motivoBloqueio = { cor: 'vermelho', mensagem: `Pré-requisito pendente: ${preRequisitoCodigo}` }
    } else if (conflitoHorario) {
      motivoBloqueio = { cor: 'laranja', mensagem: `Conflito de horário com ${conflitoHorario.codigoDaDisciplina}` }
    } else if (excedeCreditos) {
      motivoBloqueio = {
        cor: 'laranja',
        mensagem: `Limite de créditos excedido (${creditosAtuais + creditos}/${LIMITE_CREDITOS})`,
      }
    }
  }

  const podeInscrever = disponivelParaInscricao && motivoBloqueio === null

  const [processando, setProcessando] = useState(false)
  const [erro, setErro] = useState('')
  const [mostrarDetalhes, setMostrarDetalhes] = useState(false)

  async function handleInscrever() {
    setProcessando(true)
    setErro('')
    try {
      await onInscrever(id)
    } catch (err: any) {
      setErro(err.response?.data?.message || 'Não foi possível se inscrever.')
    } finally {
      setProcessando(false)
    }
  }

  let rotuloBotao: string
  let classeBotao: string

  if (matriculaStatus === 'INSCRITO') {
    rotuloBotao = 'Inscrito'
    classeBotao = 'bg-green-100 text-green-700 cursor-default'
  } else if (matriculaStatus === 'CANCELADA') {
    rotuloBotao = 'Cancelada'
    classeBotao = 'bg-ui-bg text-ui-muted cursor-default'
  } else if (matriculaStatus === 'CONCLUIDA') {
    rotuloBotao = 'Concluída'
    classeBotao = 'bg-brand-light text-brand-primary cursor-default'
  } else if (matriculaStatus === 'REPROVADA') {
    rotuloBotao = 'Reprovada'
    classeBotao = 'bg-red-100 text-red-700 cursor-default'
  } else if (podeInscrever) {
    rotuloBotao = processando ? 'Inscrevendo...' : 'Inscrever-se'
    classeBotao = 'bg-brand-primary text-white hover:bg-indigo-700 active:bg-indigo-800'
  } else if (motivoBloqueio) {
    rotuloBotao = 'Indisponível'
    classeBotao =
      motivoBloqueio.cor === 'vermelho' ? 'bg-red-100 text-red-700 cursor-default' : 'bg-amber-100 text-amber-700 cursor-default'
  } else {
    rotuloBotao = 'Indisponível'
    classeBotao = 'bg-ui-bg text-ui-muted cursor-default'
  }

  return (
    <div className="bg-white border border-ui-border rounded-xl p-5 flex flex-col gap-3">
      <div>
        <h3 className="font-semibold text-base text-ui-dark leading-5">{nome}</h3>
        <p className="text-xs text-ui-muted mt-0.5">{codigo}</p>
      </div>

      <div className="flex flex-wrap gap-x-4 gap-y-1 text-sm text-ui-medium">
        <span>{creditos} créditos</span>
        <span>{vagas} vagas</span>
        <span>{horario}</span>
      </div>

      <button
        onClick={() => setMostrarDetalhes(true)}
        className="text-xs font-medium text-brand-primary hover:underline self-start"
      >
        Ver detalhes
      </button>

      {preRequisitoCodigo && (
        <p className={`text-xs font-medium ${preRequisitoAtendido ? 'text-green-700' : 'text-red-700'}`}>
          {preRequisitoAtendido
            ? `Pré-requisito atendido (${preRequisitoCodigo})`
            : `Pré-requisito pendente: ${preRequisitoCodigo}`}
        </p>
      )}

      {motivoBloqueio && preRequisitoAtendido && (
        <p className="text-xs font-medium text-amber-700">{motivoBloqueio.mensagem}</p>
      )}

      <button
        onClick={podeInscrever ? handleInscrever : undefined}
        disabled={!podeInscrever || processando}
        className={`mt-1 w-full text-sm font-medium px-4 py-2 rounded-lg transition-colors disabled:opacity-100 disabled:cursor-not-allowed ${classeBotao}`}
      >
        {rotuloBotao}
      </button>

      {erro && <p className="text-xs font-medium text-red-600">{erro}</p>}

      {mostrarDetalhes && <DisciplinaDetalhesModal disciplinaId={id} onClose={() => setMostrarDetalhes(false)} />}
    </div>
  )
}
