package com.catijr.backend.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name ="registration_model")
public class RegistrationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name="disciplie_id", nullable = false)
    private DisciplineModel discipline;

    @Column
    private LocalDateTime registrationDate;

    @Column(nullable = false, length = 100)
    private String status;


}
