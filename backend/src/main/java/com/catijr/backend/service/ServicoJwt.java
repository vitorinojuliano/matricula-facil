package com.catijr.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ServicoJwt {

    @Value("${jwt.secret}")
    private String senhaSecretaJwt;

    @Value("${jwt.issuer}")
    private String emissorToken;

    @Value("${jwt.expirationTime}")
    private long tempoExpiracaoEmMinutos;


    public String gerarToken(String email) {
        Algorithm algoritmoSegurancaHmac256 = Algorithm.HMAC256(senhaSecretaJwt);

        return JWT.create()
                .withIssuer(emissorToken)
                .withSubject(email)
                .withExpiresAt(Instant.now().plus(tempoExpiracaoEmMinutos, ChronoUnit.MINUTES))
                .withIssuedAt(Instant.now())
                .sign(algoritmoSegurancaHmac256);
    }

    public boolean validarToken(String token) {
        try {
            Algorithm algoritmoSegurancaHmac256 = Algorithm.HMAC256(senhaSecretaJwt);
            JWT.require(algoritmoSegurancaHmac256)
                    .withIssuer(emissorToken)
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String extrairEmailDoToken(String token) {
        Algorithm algoritmoSegurancaHmac256 = Algorithm.HMAC256(senhaSecretaJwt);
        return JWT.require(algoritmoSegurancaHmac256)
                .withIssuer(emissorToken)
                .build()
                .verify(token)
                .getSubject();
    }
}
