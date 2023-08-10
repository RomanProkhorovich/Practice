package com.example.practice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(title = "Задание для практики", description = "Я старался, надеюсь багов немного. если что, не бейте пж((",
                contact = @Contact(name = "Прохорович Роман", email = "pomaprokhorovich583@gmail.com"))
)
@SecurityScheme(name = "BearerJWt",type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, description = "JWT token in 'Authorization' header")
@SpringBootApplication
public class PracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticeApplication.class, args);
    }

}
