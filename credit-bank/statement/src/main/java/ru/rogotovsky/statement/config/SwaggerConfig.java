package ru.rogotovsky.statement.config;

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
                                .title("Statement Microservice API")
                                .description("""
                                        REST API for handling loan application workflow.

                                        The service is responsible for:
                                         - receiving initial loan application requests,
                                         - performing prescoring validation,
                                         - sending requests to Deal microservice,
                                         - returning loan offers to the client,
                                         - forwarding selected loan offers for further processing.

                                        This microservice acts as an entry point for loan application flow.
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
