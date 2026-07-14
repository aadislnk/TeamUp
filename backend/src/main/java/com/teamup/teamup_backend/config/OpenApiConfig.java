package com.teamup.teamup_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI teamUpOpenAPI() {

        Contact contact = new Contact()
                .name("Adityaraj Solanki")
                .email("adityaslnk45035@gmail.com")
                .url("https://github.com/aadislnk");

        Info info = new Info()
                .title("TeamUp API")
                .description("TeamUp is a platform where students can discover hackathons and technical events, create or join teams, recruit members, and collaborate.")
                .version("1.0.0")
                .contact(contact);

        return new OpenAPI()
                .info(info);
    }
}