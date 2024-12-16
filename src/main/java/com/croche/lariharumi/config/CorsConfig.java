package com.croche.lariharumi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String frontendUrl = System.getenv("FRONTEND_URL"); // Recupera o valor da variável de ambiente

        if (frontendUrl != null) {
            registry.addMapping("/**")
                    .allowedOrigins(frontendUrl) // Usa a variável de ambiente para definir o domínio permitido
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        } else {
            // Caso a variável de ambiente não esteja definida, podemos permitir qualquer origem ou configurar um valor padrão
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173") // Valor padrão
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }
}