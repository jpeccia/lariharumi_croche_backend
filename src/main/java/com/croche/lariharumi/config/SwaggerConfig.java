package com.croche.lariharumi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
        .info(new Info().title("LariCroche").description("Esta API é um sistema completo de gerenciamento e autenticação de usuários, com foco em segurança e controle de acesso. Ela oferece uma variedade de funcionalidades, incluindo o registro, login e a autenticação de usuários, bem como o gerenciamento de produtos em um catálogo. A API utiliza autenticação baseada em tokens JWT para garantir a segurança e o acesso restrito às rotas protegidas.").version("1"))
        .schemaRequirement("jwt_auth", creaSecurityScheme());
    }

    private SecurityScheme creaSecurityScheme(){
        return new SecurityScheme().name("jwt_auth").type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT");
    }
}