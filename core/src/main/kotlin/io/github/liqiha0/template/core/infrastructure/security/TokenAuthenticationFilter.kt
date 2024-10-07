package io.github.liqiha0.template.core.infrastructure.security

import io.github.liqiha0.template.core.domain.repository.TokenRepository
import io.github.liqiha0.template.core.domain.specification.TokenSpecification.accessTokenEqual
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import kotlin.jvm.optionals.getOrNull

class TokenAuthenticationFilter(
    private val tokenRepository: TokenRepository,
) : AbstractPreAuthenticatedProcessingFilter() {

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Any? {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
        if (token != null) {
            val tokenEntity = tokenRepository.findOne(accessTokenEqual(token)).getOrNull()
            return tokenEntity?.principalId
        } else {
            return null
        }
    }

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest): Any? {
        return "N/A"
    }
}