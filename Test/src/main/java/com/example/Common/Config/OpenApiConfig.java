package com.example.Common.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Open API test",
                description = "Open API",
                contact = @Contact(name = "admin", email = "admin@gmail.com"),
                license = @License(name = "License name", url = "someURl.com"),
                version = "1.0.0"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server"),
        },
        security = {
                @SecurityRequirement(name = "bearerAuth", scopes = {"read", "write"})
        }
)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                description = "JWT and description",
                scheme = "bearer",
                type = SecuritySchemeType.HTTP,
                in = SecuritySchemeIn.HEADER
        ),
        // Định nghĩa thêm các SecurityScheme khác
})
public class OpenApiConfig {

}
