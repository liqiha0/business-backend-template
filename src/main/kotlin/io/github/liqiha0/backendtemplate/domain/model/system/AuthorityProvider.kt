package io.github.liqiha0.backendtemplate.domain.model.system

import org.springframework.stereotype.Component

interface AuthorityProvider {
    val authorities: List<Authority>
}

@Component
class AdminAuthorityProvider : AuthorityProvider {
    override val authorities: List<Authority> = listOf(Administrator)
}