package io.github.liqiha0.template.core.domain.model.iam

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class PrincipalFactory(private val passwordEncoder: PasswordEncoder) {

    fun createWithUsernamePassword(username: String, password: String, roleIds: Set<UUID> = emptySet()): Principal {
        require(username.isNotBlank())
        require(password.isNotBlank())

        val identity = UsernameIdentity(username)
        val passwordHash = passwordEncoder.encode(password)
        val credential = PasswordCredential(passwordHash)

        return Principal(
            roleIds = roleIds,
            identities = mutableSetOf(identity),
            credentials = mutableSetOf(credential)
        )
    }

    fun createWithPhone(
        phone: String,
        roleIds: Set<UUID> = emptySet()
    ): Principal {
        require(phone.isNotBlank())

        val identity = PhoneIdentity(phone)
        return Principal(
            roleIds = roleIds,
            identities = mutableSetOf(identity),
            credentials = mutableSetOf()
        )
    }

    fun createWithWechat(
        openId: String,
        unionId: String? = null,
        roleIds: Set<UUID> = emptySet()
    ): Principal {
        require(openId.isNotBlank())

        val identity = WechatIdentity(openId, unionId)
        return Principal(
            roleIds = roleIds,
            identities = mutableSetOf(identity),
            credentials = mutableSetOf()
        )
    }
}
