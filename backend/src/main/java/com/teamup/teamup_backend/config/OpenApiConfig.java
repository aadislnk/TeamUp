package com.teamup.teamup_backend.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADERgit
)
public class OpenApiConfig {

    @Bean
    public OpenAPI teamUpOpenAPI() {

        Contact contact = new Contact()
                .name("Adityaraj Solanki")
                .email("adityaslnk45035@gmail.com")
                .url("https://github.com/aadislnk");

        Info info = new Info()
                .title("TeamUp API")
                .version("1.0.0")
                .description("""
                        TeamUp is a platform where students can discover hackathons and technical events,
                        create or join teams, recruit members, and collaborate.
                        """)
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}