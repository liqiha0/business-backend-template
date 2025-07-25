package io.github.liqiha0.template.core.domain.model.iam

import jakarta.persistence.*
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Credential() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID
        internal set
}

@Entity
class UsernamePasswordCredential(
    val username: String,
    var passwordHash: String
) : Credential()

@Entity
class WechatCredential(
    val openId: String,
    var sessionKey: String,
    val unionId: String? = null
) : Credential()

@Entity
class PhoneCredential(
    val phone: String
) : Credential()
