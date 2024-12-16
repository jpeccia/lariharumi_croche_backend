package com.croche.lariharumi.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

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

            // Geração do token com data de expiração, assunto e role do usuário
            return JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("role", user.getRole().name())  // Adiciona "ROLE_" ao valor da role
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao criar token", exception);
        }
    }

    // Método para extrair a role do token
    public String extractRoleFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var verifier = JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build();

            var decodedJWT = verifier.verify(token);
            String role = decodedJWT.getClaim("role").asString();
            
            // Adicionando log para verificar o valor da role
            System.out.println("Role extraída do token: " + role);
            return role;  // Retorna a role extraída
        } catch (JWTVerificationException exception) {
            // Log de erro se o token for inválido ou não contiver a role
            System.out.println("Erro ao extrair a role do token: " + exception.getMessage());
            return null;
        }
    }

    // Validação e verificação do token
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token);  // Verifica se o token é válido
    
            System.out.println("Token válido para o email: " + decodedJWT.getSubject());
    
            // Verifica se o token está expirado
            Date expirationDate = decodedJWT.getExpiresAt();
            if (expirationDate != null && expirationDate.toInstant().isBefore(Instant.now())) {
                System.out.println("Token expirado.");
                return null;  // Token expirado
            }
    
            return decodedJWT.getSubject();  // Retorna o email do usuário (subject)
        } catch (JWTVerificationException exception) {
            System.out.println("Erro de validação do token: " + exception.getMessage());
            return null;  // Caso o token seja inválido
        }
    }
    

    // Método que define a data de expiração do token
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.UTC);  // Expira em 8 horas
    }
    
}
