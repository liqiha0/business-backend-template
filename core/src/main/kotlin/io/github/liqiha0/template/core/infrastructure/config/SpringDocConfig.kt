package io.github.liqiha0.template.core.infrastructure.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.security.SecuritySchemes
import org.springframework.context.annotation.Configuration

@Configuration
@SecuritySchemes(
    SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        `in` = SecuritySchemeIn.HEADER
    )
)
@OpenAPIDefinition(
    security = [SecurityRequirement(name = "bearerAuth")]
)
class SpringDocConfig

