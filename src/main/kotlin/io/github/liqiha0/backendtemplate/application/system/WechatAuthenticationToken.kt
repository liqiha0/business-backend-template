package io.github.liqiha0.backendtemplate.application.system

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import java.util.*

class WechatAuthenticationToken(private val credentials: String, private val principal: UUID? = null) :
    AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {

    override fun getCredentials(): Any = this.credentials

    override fun getPrincipal(): Any? = this.principal
}