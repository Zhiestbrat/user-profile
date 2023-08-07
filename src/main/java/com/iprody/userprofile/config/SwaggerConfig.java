package com.iprody.userprofile.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
        info = @Info(
                title = "User Profile API",
                description = "Documentation for User Profile API", version = "0.0.1-SNAPSHOT",
                termsOfService = "https://iprody.com/user-profile-service"
        )
)
public class SwaggerConfig {
}