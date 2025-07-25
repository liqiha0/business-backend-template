package io.github.liqiha0.template.core.infrastructure

import io.github.liqiha0.template.core.infrastructure.security.TokenAuthenticationFilter
import org.springframework.http.HttpStatus
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
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.anonymous { it.disable() }
        http.exceptionHandling {
            it.authenticationEntryPoint { request, response, authException ->
                response.sendError(HttpStatus.UNAUTHORIZED.value())
            }
            it.accessDeniedHandler { request, response, accessDeniedException ->
                response.sendError(HttpStatus.FORBIDDEN.value())
            }
        }
        http.addFilter(tokenFilter)
    }
}