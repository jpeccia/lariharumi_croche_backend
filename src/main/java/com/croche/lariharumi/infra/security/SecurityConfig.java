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
                    // Permitir rotas públicas como login, registro e Swagger
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()  // Permite o login sem autenticação
                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()  // Permite o registro sem autenticação
                    .requestMatchers(SWAGGER_LIST).permitAll()  // Permite acesso ao Swagger UI
                    // Apenas usuários com a role ROLE_ADMIN têm acesso às rotas de criação, atualização e remoção de produtos e categorias
                    .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.POST, "/products").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.POST, "/products/{id}/upload-image").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/products/{id}").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/products/{id}").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/products/{id}/images/{imageIndex}").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.POST, "/categories").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.POST, "/categories/{id}/upload-image").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/categories/{id}").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/categories/{id}").hasAuthority("ROLE_ADMIN")
                    // As rotas do admin devem ser protegidas e acessíveis apenas para "ROLE_ADMIN"
                    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    // Outras rotas podem ser acessadas por qualquer usuário autenticado
                    .anyRequest().permitAll())  // Permite o acesso a todas as outras rotas sem autenticação
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