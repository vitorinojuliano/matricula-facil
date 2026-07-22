import { useEffect, useState } from 'react'
import DashboardHeader from '../components/DashboardHeader'
import CatalogHeading from '../components/CatalogHeading'
import DisciplinaCard from '../components/DisciplinaCard'
import MatriculaRow from '../components/MatriculaRow'
import PerfilView from '../components/PerfilView'
import api from '../services/api'
import { DashboardView, Disciplina, Matricula, User } from '../types'

interface DashboardPageProps {
  user: User
  onLogout: () => void
  onUserUpdate: (usuarioAtualizado: User) => void
}


const SEMESTRE_ATUAL = 2
const ANO_ATUAL = 2026

export default function DashboardPage({ user, onLogout, onUserUpdate }: DashboardPageProps) {
  const [view, setView] = useState<DashboardView>('catalogo')

  const [disciplinas, setDisciplinas] = useState<Disciplina[]>([])
  const [carregandoDisciplinas, setCarregandoDisciplinas] = useState(true)
  const [erroDisciplinas, setErroDisciplinas] = useState('')

  const [matriculas, setMatriculas] = useState<Matricula[]>([])
  const [creditosAtuais, setCreditosAtuais] = useState(0)
  const [carregandoMatriculas, setCarregandoMatriculas] = useState(true)
  const [erroMatriculas, setErroMatriculas] = useState('')

  function carregarDisciplinas() {
    return api
      .get<Disciplina[]>('/disciplina')
      .then((resposta) => setDisciplinas(resposta.data))
      .catch(() => setErroDisciplinas('Não foi possível carregar o catálogo de disciplinas.'))
  }

  function carregarMatriculas() {
    return api
      .get<{ matriculas: Matricula[]; creditosAtuais: number }>('/matricula', {
        params: { semestre: SEMESTRE_ATUAL, ano: ANO_ATUAL },
      })
      .then((resposta) => {
        setMatriculas(resposta.data.matriculas)
        setCreditosAtuais(resposta.data.creditosAtuais)
      })
      .catch(() => setErroMatriculas('Não foi possível carregar suas matérias.'))
  }

  useEffect(() => {
    carregarDisciplinas().finally(() => setCarregandoDisciplinas(false))
    carregarMatriculas().finally(() => setCarregandoMatriculas(false))
  }, [])

  const matriculasInscritas = matriculas.filter((m) => m.status === 'INSCRITO')

  const [filtroSemestre, setFiltroSemestre] = useState<number | 'todos'>('todos')
  const [filtroAno, setFiltroAno] = useState<number | 'todos'>('todos')

  const semestresDisponiveis = Array.from(new Set(disciplinas.map((d) => d.semestre))).sort()
  const anosDisponiveis = Array.from(new Set(disciplinas.map((d) => d.ano))).sort()

  const [buscaTexto, setBuscaTexto] = useState('')

  const disciplinasFiltradas = disciplinas.filter((d) => {
    const correspondeSemestre = filtroSemestre === 'todos' || d.semestre === filtroSemestre
    const correspondeAno = filtroAno === 'todos' || d.ano === filtroAno
    const termo = buscaTexto.trim().toLowerCase()
    const correspondeBusca = termo === '' || d.nome.toLowerCase().includes(termo) || d.codigo.toLowerCase().includes(termo)
    return correspondeSemestre && correspondeAno && correspondeBusca
  })

  async function handleInscrever(disciplinaId: number) {
    await api.post('/matricula', { disciplinaId })
    await Promise.all([carregarDisciplinas(), carregarMatriculas()])
  }

  async function handleCancelar(matriculaId: number) {
    await api.delete(`/matriculas/${matriculaId}`)
    await Promise.all([carregarDisciplinas(), carregarMatriculas()])
  }

  return (
    <div className="min-h-screen bg-ui-bg">
      <DashboardHeader user={user} onLogout={onLogout} activeView={view} onNavigate={setView} />

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-10">
        {view === 'catalogo' && (
          <>
            <CatalogHeading semestre="atual" />

            {carregandoDisciplinas && <p className="mt-8 text-sm text-ui-muted">Carregando disciplinas...</p>}

            {!carregandoDisciplinas && erroDisciplinas && (
              <p className="mt-8 text-sm text-red-600">{erroDisciplinas}</p>
            )}

            {!carregandoDisciplinas && !erroDisciplinas && (
              <>
                <input
                  type="text"
                  value={buscaTexto}
                  onChange={(e) => setBuscaTexto(e.target.value)}
                  placeholder="Buscar por nome ou código..."
                  className="mt-8 w-full max-w-md border border-ui-border rounded-lg px-3 py-2 text-sm text-ui-dark bg-white"
                />

                <div className="mt-3 flex flex-wrap gap-3">
                  <select
                    value={filtroSemestre}
                    onChange={(e) => setFiltroSemestre(e.target.value === 'todos' ? 'todos' : Number(e.target.value))}
                    className="border border-ui-border rounded-lg px-3 py-2 text-sm text-ui-dark bg-white"
                  >
                    <option value="todos">Todos os semestres</option>
                    {semestresDisponiveis.map((semestre) => (
                      <option key={semestre} value={semestre}>
                        Semestre {semestre}
                      </option>
                    ))}
                  </select>

                  <select
                    value={filtroAno}
                    onChange={(e) => setFiltroAno(e.target.value === 'todos' ? 'todos' : Number(e.target.value))}
                    className="border border-ui-border rounded-lg px-3 py-2 text-sm text-ui-dark bg-white"
                  >
                    <option value="todos">Todos os anos</option>
                    {anosDisponiveis.map((ano) => (
                      <option key={ano} value={ano}>
                        {ano}
                      </option>
                    ))}
                  </select>
                </div>

                {disciplinasFiltradas.length === 0 ? (
                  <p className="mt-4 text-sm text-ui-muted">Nenhuma disciplina encontrada para esse filtro.</p>
                ) : (
                  <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
                    {disciplinasFiltradas.map((disciplina) => (
                      <DisciplinaCard
                        key={disciplina.id}
                        disciplina={disciplina}
                        creditosAtuais={creditosAtuais}
                        matriculasInscritas={matriculasInscritas}
                        onInscrever={handleInscrever}
                      />
                    ))}
                  </div>
                )}
              </>
            )}
          </>
        )}

        {view === 'materias' && (
          <>
            <div className="flex flex-col gap-1">
              <h1 className="font-bold text-[28px] sm:text-[32px] text-ui-dark tracking-tight leading-tight">
                Minhas Matérias
              </h1>
              <p className="text-base text-ui-muted leading-6">
                Semestre {SEMESTRE_ATUAL}/{ANO_ATUAL} — Créditos atuais:{' '}
                <span className="font-medium text-brand-accent">{creditosAtuais}/24</span>
              </p>
            </div>

            {carregandoMatriculas && <p className="mt-8 text-sm text-ui-muted">Carregando suas matérias...</p>}

            {!carregandoMatriculas && erroMatriculas && (
              <p className="mt-8 text-sm text-red-600">{erroMatriculas}</p>
            )}

            {!carregandoMatriculas && !erroMatriculas && matriculas.length === 0 && (
              <p className="mt-8 text-sm text-ui-muted">Você não tem matrículas no semestre atual.</p>
            )}

            {!carregandoMatriculas && !erroMatriculas && matriculas.length > 0 && (
              <div className="mt-8 flex flex-col gap-3">
                {matriculas.map((matricula) => (
                  <MatriculaRow key={matricula.id} matricula={matricula} onCancelar={handleCancelar} />
                ))}
              </div>
            )}
          </>
        )}

        {view === 'perfil' && (
          <>
            <h1 className="font-bold text-[28px] sm:text-[32px] text-ui-dark tracking-tight leading-tight">
              Meu Perfil
            </h1>
            <PerfilView onEmailAtualizado={(novoEmail) => onUserUpdate({ id: user.id, email: novoEmail })} />
          </>
        )}
      </main>
    </div>
  )
}
