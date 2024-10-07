package io.github.liqiha0.template.core.infrastructure.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AccessTokenAuthenticationToken(
    val token: String,
    authorities: Collection<GrantedAuthority> = emptyList(),
    private var principalObj: Any? = null,
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any? = token
    override fun getPrincipal(): Any? = principalObj ?: token
    fun setPrincipal(principal: Any?) { this.principalObj = principal }
}
