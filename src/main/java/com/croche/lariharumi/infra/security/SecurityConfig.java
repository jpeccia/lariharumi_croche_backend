package com.croche.lariharumi.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    private static final String[] SWAGGER_LIST = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
               .csrf(csrf -> csrf.disable())  // Desabilita CSRF, útil para APIs REST
               .cors()  // Habilita CORS se necessário
               .and()
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Desabilita sessões, usando JWT ou outro token
               .authorizeHttpRequests(authorize -> authorize
                    // Permitir rotas públicas, como login, registro e Swagger
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                    .requestMatchers(SWAGGER_LIST).permitAll()
                    // Apenas usuários com a role "ADMIN" podem acessar a dashboard personalizada
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    // Outras rotas são acessíveis para qualquer usuário autenticado (se preferir autenticação opcional, remova essa linha)
                    .anyRequest().authenticated())
               .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)  // Filtro de segurança personalizado para validar tokens, por exemplo, JWT
               .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Utiliza o BCrypt para criptografar senhas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // Configura o AuthenticationManager
    }
}
