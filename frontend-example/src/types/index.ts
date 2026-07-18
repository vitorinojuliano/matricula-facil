export type Page = 'login' | 'signup' | 'dashboard'

// Bate com RespostaPerfil (GET /user/me) e o campo "user" de POST /login no backend
export interface User {
  id: number
  email: string
}

// Bate com RespostaDisciplinaCard (GET /disciplina) no backend
export interface Disciplina {
  id: number
  nome: string
  codigo: string
  creditos: number
  vagas: number
  horario: string
  status: string
  semestre: number
  ano: number
  preRequisitoCodigo: string | null
  preRequisitoStatus: string | null
  matriculaId: number | null
  matriculaStatus: string | null
}

// Bate com PreRequisitoDTO no backend
export interface PreRequisitoDetalhe {
  codigo: string
  nome: string
  status: string
}

// Bate com RespostaDisciplinaDetalhes (GET /disciplina/{id}) no backend
export interface DisciplinaDetalhes extends Disciplina {
  professor: string
  descricao: string
  preRequisitosDetalhados: PreRequisitoDetalhe[]
}

export type DashboardView = 'catalogo' | 'materias' | 'perfil'

// Bate com SolicitacaoAtualizacaoPerfil (PUT /user/me) no backend
// RespostaPerfil (GET /user/me) é {id, email} — mesmo formato do tipo User, reaproveitado.
export interface AtualizacaoPerfilRequest {
  email?: string
  senhaAtual?: string
  novaSenha?: string
}

// Bate com RespostaMatricula (GET /matricula) no backend
export interface Matricula {
  id: number
  nomeDaMatricula: string
  codigoDaDisciplina: string
  creditos: number
  horario: string
  status: string
  dataMatricula: string
  semestre: number
  ano: number
}
