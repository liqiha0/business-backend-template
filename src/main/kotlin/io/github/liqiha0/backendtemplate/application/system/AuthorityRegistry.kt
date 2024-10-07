package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.Authority
import org.springframework.stereotype.Service

@Service
class AuthorityRegistry {
    private val authorities: MutableMap<String, Authority> = mutableMapOf()

    fun registerAuthority(authority: Authority) {
        if (authorities.containsKey(authority.key)) {
            throw IllegalStateException("重复注册Authority Key：" + authority.key)
        }
        authorities[authority.key] = authority
    }

    fun getAuthority(key: String): Authority? {
        return authorities[key]
    }

    fun getAllAuthorities(): Collection<Authority> {
        return authorities.values
    }
}