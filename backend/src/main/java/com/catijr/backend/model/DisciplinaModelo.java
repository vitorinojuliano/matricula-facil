package com.catijr.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "disciplina_modelo",
        uniqueConstraints = @UniqueConstraint(name = "uk_disciplina_codigo_periodo", columnNames = {"codigo", "ano", "semestre"})
)
@Check(constraints = "semestre in (1, 2)")
public class DisciplinaModelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private Integer creditos;

    @Column(nullable = false)
    private Integer vagas;

    @Column(nullable = false, length = 255)
    private String horario;

    @Column(nullable = false, length = 255)
    private String status;

    @ManyToOne
    @JoinColumn(name = "pre_requisito_id")
    private DisciplinaModelo preRequisito;

    @Column(nullable = false, length = 255)
    private String professor;
    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false, length = 255)
    private Integer semestre;

    @Column(nullable = false, length = 255)
    private Integer ano;


}
