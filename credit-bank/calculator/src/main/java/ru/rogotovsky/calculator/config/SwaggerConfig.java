package ru.rogotovsky.calculator.config;

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
                                .title("Credit Calculator API")
                                .description(
                                        "REST API for loan offer generation and full credit calculation. " +
                                                "The service performs prescoring and scoring of client data, " +
                                                "calculates the final interest rate, monthly payment, total " +
                                                "credit cost (PSK), and generates a full payment schedule."
                                )
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Rogotovsky Dmitry")
                                                .email("drogotovsky@gmail.com")
                                )
                );
    }
}
