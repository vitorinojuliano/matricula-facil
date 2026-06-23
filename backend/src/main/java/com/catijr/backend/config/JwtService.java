package com.catijr.backend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    // apenas para teste, se for para algo mais serio colocar uma senha melhor e também esconder
    private static final String JWT_SECRET = "matriculafacil";
    private static final long EXPIRATION_TIME = 60;

    public String generateToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);

        return JWT.create()
                .withIssuer("MatriculaFacil")
                .withSubject(email)
                .withExpiresAt(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.MINUTES))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWT.require(algorithm)
                    .withIssuer("MatriculaFacil")
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
        return JWT.require(algorithm)
                .withIssuer("MatriculaFacil")
                .build()
                .verify(token)
                .getSubject();
    }
}
