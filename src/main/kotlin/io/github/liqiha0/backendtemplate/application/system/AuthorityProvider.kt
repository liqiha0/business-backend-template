package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.Administrator
import org.springframework.stereotype.Component

interface AuthorityProvider {
    fun registerAuthorities(registry: AuthorityRegistry)
}

@Component
class AdminAuthorityProvider : AuthorityProvider {
    override fun registerAuthorities(registry: AuthorityRegistry) {
        registry.registerAuthority(Administrator)
    }
}