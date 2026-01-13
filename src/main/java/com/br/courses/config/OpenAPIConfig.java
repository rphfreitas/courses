package com.br.courses.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Courses Service API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de cursos com operações CRUD completas")
                        .contact(new Contact()
                                .name("Courses Team")
                                .email("support@coursesapi.com")
                                .url("https://github.com/seu-usuario/courses-service"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local - Desenvolvimento"),
                        new Server()
                                .url("https://api.coursesservice.com")
                                .description("Servidor de Produção")
                ));
    }
}

