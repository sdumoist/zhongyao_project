package com.xpxp.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "未知项目",
                version = "1.0",
                description = "项目文档",
                contact = @Contact(name = "px")
        ),
        security = @SecurityRequirement(name = "jwt")
)
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "jwt", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class Swagger3Config {

}
