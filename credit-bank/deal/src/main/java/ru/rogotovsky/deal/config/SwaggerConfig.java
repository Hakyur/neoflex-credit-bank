package ru.rogotovsky.deal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI calculatorOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Deal Microservice API")
                                .description("""
                                        REST API for managing loan statements, selected offers, and credits.
                                        The service performs:
                                         - creation and storage of client and statement data,
                                         - sending requests to Calculator API to get loan offers,
                                         - handling user selection of loan offers,
                                         - final credit calculation and storage of Credit entity,
                                         - tracking of statement and credit statuses,
                                         - logging all steps and changes of statements.
                                        """)
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Rogotovsky Dmitry")
                                                .email("drogotovsky@gmail.com")
                                )
                );
    }
}
