package com.accoladehq.calendar.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Calendar Booking System API")
                        .version("1.0.0")
                        .description("Use uid in 11111111-1111-1111-1111-111111111111 format in the request header. " +
                                    "OR check src/main/resources/data.sql for sample data." +
                                    "The application uses h2 in-memory database with postgres dialect to store and query data"
                        ));
    }
}
