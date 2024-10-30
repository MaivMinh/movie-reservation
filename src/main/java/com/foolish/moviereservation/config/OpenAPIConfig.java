package com.foolish.moviereservation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Movie Reservation API Documentations",
                contact = @Contact(
                        name = "Mai Van Minh",
                        email = "maivanminh.se@gmail.com",
                        url = "https://www.linkedin.com/in/minhus"
                ),
                version = "v1.0.1"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local server"
                ),
                @Server(
                        url = "https://real-server-here.com",
                        description = "Real server will stay here"
                )
        }
)
@SecurityScheme(
        name = "access_token",
        in = SecuritySchemeIn.COOKIE,
        type = SecuritySchemeType.APIKEY
)
public class OpenAPIConfig {
}
