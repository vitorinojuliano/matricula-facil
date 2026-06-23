package com.catijr.backend.service;

import com.catijr.backend.dto.RegistrationRequest;
import com.catijr.backend.exception.DisciplineNotFoundException;
import com.catijr.backend.exception.UserNotFoundException;
import com.catijr.backend.model.DisciplineModel;
import com.catijr.backend.model.RegistrationModel;
import com.catijr.backend.model.UserModel;
import com.catijr.backend.repository.DisciplineRepository;
import com.catijr.backend.repository.RegistrationRepository;
import com.catijr.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    // tratar essas exceções depois
    public RegistrationModel registration(Long userId, RegistrationRequest request) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("Usuário não encontrado"));

        DisciplineModel discipline = disciplineRepository.findById(request.getDisciplineId())
                .orElseThrow(()-> new DisciplineNotFoundException("Disciplina não encontrada"));

        if(registrationRepository.existsByUserIdAndDisciplineId(userId, discipline.getId())) {
            throw new RuntimeException("Aluno já matriculado");
        }
        if(discipline.getVagas() <= 0){
            throw new RuntimeException("Sem vagas");
        }
        if(discipline.getPre_requisito() != null && !discipline.getPre_requisito().equals("Nenhum")){
            boolean hasPrerequiste = registrationRepository.existsByUserIdAndDisciplineCodeAndStatus(userId, discipline.getPre_requisito());

            if(!hasPrerequiste){
                throw new RuntimeException("Sem pre-requisito de " + discipline.getPre_requisito());
            }
        }

        Integer currentCredits = registrationRepository.sumCreditByUserId(userId);
        if (currentCredits <= 0){
            currentCredits =0;
        }
        if(currentCredits + discipline.getCreditos() > 24){
            throw new RuntimeException("Limite de créditos atingido(MAX 24 creditos)");
        }

        RegistrationModel registration = new RegistrationModel();
        registration.setUser(user);
        registration.setDiscipline(discipline);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus("INSCRITO");

        discipline.setVagas(discipline.getVagas()-1);
        disciplineRepository.save(discipline);

        return registrationRepository.save(registration);
    }
    public List<RegistrationModel> listRegistrationForUser(Long userId) {
        return registrationRepository.findByUserId(userId);
    }

    public void cancelRegistration(long registrationId, long userId){
        RegistrationModel registration = registrationRepository
                .findByIdAndUserId(registrationId, userId)
                .orElseThrow(()-> new DisciplineNotFoundException("Matricula não encontrada"));
        if(!registration.getStatus().equals("INSCRITO")){
            throw new RuntimeException("Não pode ser cancelado");
        }

        DisciplineModel discipline = registration.getDiscipline();
        discipline.setVagas(discipline.getVagas()+1);
        disciplineRepository.save(discipline);

        registration.setStatus("CANCELADO");
        registrationRepository.save(registration);
    }
}
