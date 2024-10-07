package io.github.liqiha0.template.core.domain.service

import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.spi.AuthorityProvider
import org.springframework.stereotype.Service

@Service
class AuthorityRegistry(
    providers: List<AuthorityProvider>? = null
) {
    private val authorities: MutableMap<String, Authority> = mutableMapOf()

    init {
        providers?.forEach { this.registerAuthority(it.authorities) }
    }

    fun registerAuthority(authority: Authority) {
        if (authorities.containsKey(authority.key)) {
            throw IllegalStateException("重复注册Authority Key：" + authority.key)
        }
        authorities[authority.key] = authority
    }

    fun registerAuthority(authorities: Collection<Authority>) {
        authorities.forEach(this::registerAuthority)
    }

    fun getAuthority(key: String): Authority? {
        return authorities[key]
    }

    fun getAllAuthorities(): List<Authority> {
        return authorities.values.toList()
    }
}