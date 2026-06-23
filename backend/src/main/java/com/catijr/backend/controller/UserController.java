package com.catijr.backend.controller;

import com.catijr.backend.config.JwtService;
import com.catijr.backend.dto.LoginRequest;
import com.catijr.backend.dto.SignupRequest;
import com.catijr.backend.exception.EmailAlreadyExistsException;
import com.catijr.backend.exception.PasswordMismatchException;
import com.catijr.backend.exception.UserNotFoundException;
import com.catijr.backend.model.UserModel;
import com.catijr.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/MatriculaFacil")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService  jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request){
        try {
            UserModel user = userService.signupUser(request);
            return ResponseEntity.ok("Usuário criado com ID: " + user.getId());
        } catch (PasswordMismatchException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "PASSWORD_MISMATCH",
                    "message",  e.getMessage()
            ));
        } catch (EmailAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "EMAIL_ALREADY",
                    "message",  e.getMessage()
            ));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest  request){
        try{

            UserModel user = userService.loginUser(request);

            String token = jwtService.generateToken(user.getEmail());

            return ResponseEntity.ok(Map.of("token", token,
                    "user", Map.of("id", user.getId(), "email", user.getEmail())));
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "USER_NOT_FOUND",
                    "message",  e.getMessage()
            ));
        }

    }

//    @GetMapping("/user")
//    public ResponseEntity<?> getUser(@RequestParam String email){
//
//    }

}
