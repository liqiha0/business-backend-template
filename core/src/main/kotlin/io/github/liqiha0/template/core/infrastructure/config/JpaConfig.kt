package io.github.liqiha0.template.core.infrastructure.config

import io.github.liqiha0.template.core.utils.principalId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import java.time.ZonedDateTime
import java.util.*

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class JpaConfig {
    @Bean
    fun auditorAware(): AuditorAware<UUID> {
        return AuditorAware<UUID> {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication != null && authentication.isAuthenticated) {
                val principal = authentication.principal
                try {
                    if (principal is User) {
                        Optional.of(principal.principalId)
                    } else {
                        Optional.empty()
                    }
                } catch (e: IllegalArgumentException) {
                    Optional.empty()
                }
            } else {
                Optional.empty()
            }
        }
    }

    @Bean
    fun auditingDateTimeProvider() = DateTimeProvider { Optional.of(ZonedDateTime.now()) }
}
