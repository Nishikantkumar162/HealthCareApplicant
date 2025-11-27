package com.healthcare.appointment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI healthcareOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Healthcare Appointment API")
                        .version("1.0.0")
                        .description("REST API for Healthcare Appointment System")
                        .contact(new Contact()
                                .name("Healthcare Dev Team")
                                .email("dev-team@example.com")));
    }
}

