package com.catijr.backend.repository;

import com.catijr.backend.model.MatriculaModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RepositorioMatricula extends JpaRepository<MatriculaModelo, Long> {
    Boolean existsByUsuarioIdAndDisciplinaIdAndStatusIn(Long usuarioId, Long disciplinaId, List<String> status);

    List<MatriculaModelo> findByUsuarioIdAndStatus(Long usuarioId, String status);
    Optional<MatriculaModelo> findByIdAndUsuarioId(Long id, Long usuarioId);
    Optional<MatriculaModelo> findFirstByUsuarioIdAndDisciplinaIdOrderByIdDesc(Long usuarioId, Long disciplinaId);

    @Query("""
    SELECT SUM(d.creditos)
    FROM MatriculaModelo r
    JOIN r.disciplina d
    WHERE r.usuario.id = :usuarioId
    AND r.status = 'INSCRITO'
    """)
    Integer somarCreditosPorUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("""
    SELECT COUNT(m) > 0
    FROM MatriculaModelo m
    JOIN m.disciplina d
    WHERE m.usuario.id = :usuarioId
    AND d.codigo = :codigo
    AND m.status = :status
    """)
    boolean existsByUsuarioIdAndDisciplinaCodigoAndStatus(@Param("usuarioId") Long usuarioId, @Param("codigo") String codigo, @Param("status") String status);

    @Query("""
    SELECT m
    FROM MatriculaModelo m
    WHERE m.usuario.id = :usuarioId
      AND (:semestre IS NULL OR m.disciplina.semestre = :semestre)
      AND (:ano IS NULL OR m.disciplina.ano = :ano)
    """)
    List<MatriculaModelo> findMatriculaByFilter(@Param("usuarioId") Long usuarioId,
                                                @Param("semestre") Integer semestre,
                                                @Param("ano") Integer ano);
}
