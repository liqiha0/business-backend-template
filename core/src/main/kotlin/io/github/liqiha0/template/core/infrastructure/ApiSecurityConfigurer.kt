package io.github.liqiha0.template.core.infrastructure

import io.github.liqiha0.template.core.infrastructure.security.TokenAuthenticationFilter
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.stereotype.Component

@Component
class ApiSecurityConfigurer(
    private val authenticationManager: AuthenticationManager,
    private val tokenFilter: TokenAuthenticationFilter
) : AbstractHttpConfigurer<ApiSecurityConfigurer, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        http.authenticationManager(authenticationManager)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .anonymous { it.disable() }
            .addFilter(tokenFilter)
    }
}