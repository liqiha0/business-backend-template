package io.github.liqiha0.backendtemplate.domain.model.system

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.domain.Specification
import java.net.URL
import java.util.*

@Entity
class UserAccount(
    displayName: String,
    phoneNumber: String? = null,
    var avatar: URL? = null,
    roleIds: Set<UUID> = emptySet()
) : Account<UserAccount>(displayName, roleIds = roleIds) {

    init {
        phoneNumber?.run { check(this.isNotBlank()) }
    }

    var phoneNumber = phoneNumber
        set(value) {
            phoneNumber?.run { check(this.isNotBlank()) }
            field = value
        }

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @Schema(description = "微信绑定")
    var wechatBinding: WechatBinding? = null

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @Schema(description = "支付宝绑定")
    var alipayBinding: AlipayBinding? = null

    @PrePersist
    fun onCreate() {
        this.registerEvent(UserAccountCreatedEvent(this.id))
    }
}

data class UserAccountCreatedEvent(val userId: UUID)

@Entity
class WechatBinding(val openId: String, val sessionKey: String, unionId: String? = null) {
    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set
}

@Entity
class AlipayBinding(var accessToken: String, val openId: String) {
    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set
}

interface UserAccountRepository : AccountRepository<UserAccount>

fun phoneNumberEqual(phoneNumber: String): Specification<UserAccount> {
    return Specification<UserAccount> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<String>("phoneNumber"), phoneNumber)
    }
}

fun phoneNumberLike(phoneNumber: String): Specification<UserAccount> {
    return Specification<UserAccount> { root, query, criteriaBuilder ->
        criteriaBuilder.like(root.get<String>("phoneNumber"), phoneNumber)
    }
}

fun wechatOpenIdEqual(openId: String): Specification<UserAccount> {
    return Specification<UserAccount> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<WechatBinding>("wechatBinding").get<String>("openId"), openId)
    }
}

fun alipayOpenIdEqual(openId: String): Specification<UserAccount> {
    return Specification<UserAccount> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<WechatBinding>("alipayBinding").get<String>("openId"), openId)
    }
}
