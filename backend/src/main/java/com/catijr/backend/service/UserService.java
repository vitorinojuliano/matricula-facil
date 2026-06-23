package com.catijr.backend.service;

import com.catijr.backend.dto.LoginRequest;
import com.catijr.backend.dto.SignupRequest;
import com.catijr.backend.exception.EmailAlreadyExistsException;
import com.catijr.backend.exception.PasswordMismatchException;
import com.catijr.backend.exception.UserNotFoundException;
import com.catijr.backend.model.UserModel;
import com.catijr.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserModel signupUser(SignupRequest request) {

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Senhas diferentes");
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        UserModel user = new UserModel();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        passwordEncoder.encode(user.getPassword());
        return userRepository.save(user);
    }

    public UserModel loginUser(LoginRequest request){
        UserModel user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UserNotFoundException("Email ou senha inválidos."));
        if(!passwordEncoder.matches(request.getPassword(), (user.getPassword()))) {
            throw new UserNotFoundException("Email ou senha inválidos.");
        }
        return user;

    }
}
