package io.github.liqiha0.template.core.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.UUID

@ConfigurationProperties("core.security.superuser")
data class SuperuserProperties(
    val username: String = "root",
    val password: String = "123456",
    val enabled: Boolean = true,
    val accessToken: String = UUID.randomUUID().toString(),
    val principalId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
)
