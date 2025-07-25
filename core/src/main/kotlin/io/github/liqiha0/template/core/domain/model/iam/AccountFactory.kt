package io.github.liqiha0.template.core.domain.model.iam

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountFactory(private val passwordEncoder: PasswordEncoder) {

    fun createWithUsernamePassword(username: String, password: String, roleIds: Set<UUID> = emptySet()): Account {
        require(username.isNotBlank())
        require(password.isNotBlank())

        val passwordHash = passwordEncoder.encode(password)
        val credential = UsernamePasswordCredential(username, passwordHash)
        val account = Account(roleIds = roleIds, credentials = mutableSetOf(credential))
        return account
    }

    fun createWithPhone(
        phone: String,
        roleIds: Set<UUID> = emptySet()
    ): Account {
        require(phone.isNotBlank())

        val credential = PhoneCredential(phone)
        val account = Account(roleIds = roleIds, credentials = mutableSetOf(credential))
        return account
    }

    fun createWithWechat(
        openId: String,
        sessionKey: String,
        unionId: String? = null,
        roleIds: Set<UUID> = emptySet()
    ): Account {
        require(openId.isNotBlank())
        require(sessionKey.isNotBlank())

        val credential = WechatCredential(openId, sessionKey, unionId)
        val account = Account(roleIds = roleIds, credentials = mutableSetOf(credential))
        return account
    }
}
