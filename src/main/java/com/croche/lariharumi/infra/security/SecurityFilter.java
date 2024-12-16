package com.croche.lariharumi.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.croche.lariharumi.models.User.User;
import com.croche.lariharumi.repository.UserRepository;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        // Log do token recebido
        if (token != null) {
            logger.info("Token recebido: " + token.substring(0, Math.min(token.length(), 10)) + "..."); // Logar apenas os primeiros 10 caracteres
        } else {
            logger.warn("Nenhum token encontrado na requisição.");
        }

        // Validação do token
        String login = tokenService.validateToken(token);

        if (login != null) {
            // Recupera o usuário com base no email
            logger.info("Token válido, usuário: " + login);

            User user = userRepository.findByEmail(login)
                    .orElseThrow(() -> {
                        logger.error("Usuário não encontrado: " + login);
                        return new RuntimeException("User Not Found");
                    });

            // Aqui, a role deve ser extraída do token
            String role = tokenService.extractRoleFromToken(token);
            logger.info("Role extraída do token: " + role);

            // Criação das authorities com base na role extraída
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            // Criação do objeto de autenticação
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Usuário autenticado com role: " + authorities);
        } else {
            logger.warn("Token inválido ou expirado.");
        }

        // Continuar com o filtro da requisição
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        // Recupera o token do header Authorization
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); // Remove o prefixo "Bearer "
    }
}
