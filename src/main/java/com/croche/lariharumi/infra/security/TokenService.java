package com.croche.lariharumi.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.croche.lariharumi.models.User.User;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // Geração de token com dados do usuário
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Geração do token com data de expiração e assunto (usuário)
            return JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())  // Define a expiração
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao criar token", exception); // Exceção mais detalhada
        }
    }

    // Validação e verificação de token
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)  // Verifica se o token é válido
                    .getSubject();  // Retorna o email (subjet)
        } catch (JWTVerificationException exception) {
            return null; // Caso o token seja inválido
        }
    }

    // Método que define a data de expiração do token
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);  // Usando UTC
    }
}
