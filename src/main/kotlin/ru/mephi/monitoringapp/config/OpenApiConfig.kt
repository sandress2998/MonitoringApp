package ru.mephi.monitoringapp.config

import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/*
@Configuration
class OpenApiConfig {
    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(Info().title("API Documentation").version("1.0"))
        .servers(listOf(
            Server().url("/api/v1").description("API Version 1"),
            Server().url("/api/v2").description("API Version 2")
        ))
        .components(Components())
        .addSecurityItem(
            SecurityRequirement().addList("bearerAuth")
        )
}
*/