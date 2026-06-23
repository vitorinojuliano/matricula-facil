package com.catijr.backend.controller;

import com.catijr.backend.dto.RegistrationRequest;
import com.catijr.backend.exception.UserNotFoundException;
import com.catijr.backend.model.RegistrationModel;
import com.catijr.backend.model.UserModel;
import com.catijr.backend.repository.UserRepository;
import com.catijr.backend.service.RegistrationService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/MatriculaFacil")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/matriculas")
    public ResponseEntity<?> registrations(@RequestBody RegistrationRequest request, Authentication authentication) {
        try {
            // olhar isso melhor depois
            String email = authentication.name();

            UserModel user = userRepository
                    .findByEmail(email)
                    .orElseThrow(()->new UserNotFoundException("Usuário não encontrado"));

            RegistrationModel registration = registrationService.registration(user.getId(),request);

            return ResponseEntity.ok(Map.of(
                    "message", "Matricula registrado com sucesso",
                    "matricula",registration
            ));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of(
                    "error","MATRICULA_ERROR",
                    "message",e.getMessage())
            );
        }
    }



}
