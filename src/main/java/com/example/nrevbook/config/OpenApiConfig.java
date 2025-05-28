package com.example.nrevbook.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Book Manager API",
                version     = "v1.0",
                description = "API for managing user books with JWT security"
        )
)
public class OpenApiConfig { }
