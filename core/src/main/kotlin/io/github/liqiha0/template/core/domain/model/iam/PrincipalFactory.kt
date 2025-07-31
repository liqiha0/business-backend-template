package io.github.liqiha0.template.core.domain.model.iam

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class PrincipalFactory(private val passwordEncoder: PasswordEncoder) {

    fun createWithUsernamePassword(username: String, password: String, roleIds: Set<UUID> = emptySet()): Principal {
        require(username.isNotBlank())
        require(password.isNotBlank())

        val identities = mutableSetOf<Identity>(UsernameIdentity(username))
        val credentials = mutableSetOf<Credential>(PasswordCredential(passwordEncoder.encode(password)))

        return createPrincipal(roleIds, identities, credentials)
    }

    fun createWithPhone(phone: String, roleIds: Set<UUID> = emptySet()): Principal {
        require(phone.isNotBlank())

        val identities = mutableSetOf<Identity>(PhoneIdentity(phone))
        val credentials = mutableSetOf<Credential>()

        return createPrincipal(roleIds, identities, credentials)
    }

    fun createWithPhonePassword(phone: String, password: String, roleIds: Set<UUID> = emptySet()): Principal {
        require(phone.isNotBlank())
        require(password.isNotBlank())

        val identities = mutableSetOf<Identity>(PhoneIdentity(phone))
        val credentials = mutableSetOf<Credential>(PasswordCredential(passwordEncoder.encode(password)))

        return createPrincipal(roleIds, identities, credentials)
    }

    fun createWithWechat(openId: String, unionId: String? = null, roleIds: Set<UUID> = emptySet()): Principal {
        require(openId.isNotBlank())

        val identities = mutableSetOf<Identity>(WechatIdentity(openId, unionId))
        val credentials = mutableSetOf<Credential>()

        return createPrincipal(roleIds, identities, credentials)
    }

    fun create(
        username: String? = null,
        password: String? = null,
        phone: String? = null,
        openId: String? = null,
        unionId: String? = null,
        roleIds: Set<UUID> = emptySet()
    ): Principal {
        val identities = mutableSetOf<Identity>()
        val credentials = mutableSetOf<Credential>()

        username?.let { identities.add(UsernameIdentity(it)) }
        phone?.let { identities.add(PhoneIdentity(it)) }
        openId?.let { identities.add(WechatIdentity(it, unionId)) }

        password?.let { credentials.add(PasswordCredential(passwordEncoder.encode(it))) }

        require(identities.isNotEmpty())

        return createPrincipal(roleIds, identities, credentials)
    }

    private fun createPrincipal(
        roleIds: Set<UUID>,
        identities: MutableSet<Identity>,
        credentials: MutableSet<Credential>
    ): Principal {
        return Principal(
            roleIds = roleIds,
            identities = identities,
            credentials = credentials
        )
    }
}
