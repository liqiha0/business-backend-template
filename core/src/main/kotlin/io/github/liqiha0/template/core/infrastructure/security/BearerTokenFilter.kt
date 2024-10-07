package io.github.liqiha0.template.core.infrastructure.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class BearerTokenFilter(
    private val authenticationManager: AuthenticationManager,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        val token = header?.takeIf { it.startsWith("Bearer ", ignoreCase = true) }?.substring(7)?.trim()
        val context = SecurityContextHolder.getContext()

        if (!token.isNullOrBlank() && context.authentication?.isAuthenticated != true) {
            try {
                val authRequest = AccessTokenAuthenticationToken(token)
                val authResult = authenticationManager.authenticate(authRequest)
                if (authResult.isAuthenticated) {
                    context.authentication = authResult
                }
            } catch (_: AuthenticationException) {
            }
        }

        filterChain.doFilter(request, response)
    }
}
