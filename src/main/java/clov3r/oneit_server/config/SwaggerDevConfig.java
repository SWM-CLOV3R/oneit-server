package clov3r.oneit_server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.models.servers.Server;


import java.util.Collections;

@Configuration
@Profile("dev")
public class SwaggerDevConfig {
    private Info apiInfo() {
        return new Info()
                .title("One!t API") // Title of the API
                .description("One!t API") // Description of the API
                .version("v1.0.0"); // Version of the API
    }
    @Bean
    public OpenAPI openAPI() {

        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement)
                .info(apiInfo())
                .servers(Collections.singletonList(
                        new Server().url("https://dev-api.oneit.gift")));
    }
}
