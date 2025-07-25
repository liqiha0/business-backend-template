package io.github.liqiha0.template.core.domain.model.iam

import jakarta.persistence.*
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Identity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID
        protected set
}

@Entity
class UsernameIdentity(
    @Column(unique = true, nullable = false)
    val username: String,
) : Identity()

@Entity
class PhoneIdentity(
    @Column(unique = true, nullable = false)
    val phone: String,
) : Identity()

@Entity
class WechatIdentity(
    @Column(unique = true, nullable = false)
    val openId: String,
    @Column(unique = true, nullable = true)
    val unionId: String?,
) : Identity()
