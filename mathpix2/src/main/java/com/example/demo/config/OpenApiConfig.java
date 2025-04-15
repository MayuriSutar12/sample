package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "demo", version = "v1",
                contact = @Contact(name = "Mayuri Sutar", url = "www.perceptcs.com", email = "mayuri.s@kanzencs.com"))
       // , servers = {@Server(url = "${open-api-server.url}", description = "Default Server URL")}
)
//@SecurityScheme(
//        name = "Authorization",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer")
public class OpenApiConfig {

}