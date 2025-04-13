package com.vitorsilvafranca.mobiauto_backend_integration_interview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mobiautoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mobiauto Test API")
                        .description("API para cálculo de distâncias entre clientes e lojistas com base em CEP e coordenadas geográficas. Integrações com ViaCEP, Nominatim e OSRM.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Vitor Silva França")
                                .email("contato@vitorsilvafranca.com")
                                .url("https://github.com/vitor-silva-franca"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor Local")
                ));
    }
}
