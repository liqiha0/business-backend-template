package io.github.liqiha0.backendtemplate.domain.model.system

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.net.URL
import java.util.*

@Entity
class UserAccount(displayName: String, phoneNumber: String? = null, var avatar: URL? = null) :
    Account<UserAccount>(displayName) {

    init {
        phoneNumber?.run { check(this.isNotBlank()) }
    }

    var phoneNumber = phoneNumber
        set(value) {
            phoneNumber?.run { check(this.isNotBlank()) }
            field = value
        }

    @OneToOne(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    @Schema(description = "微信绑定")
    var wechatBinding: WechatBinding? = null

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

interface UserAccountRepository : JpaRepository<UserAccount, UUID>, JpaSpecificationExecutor<UserAccount>

fun phoneNumberEqual(phoneNumber: String): Specification<UserAccount> {
    return Specification<UserAccount> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<String>("phoneNumber"), phoneNumber)
    }
}

fun wechatOpenIdEqual(openId: String): Specification<UserAccount> {
    return Specification<UserAccount> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<WechatBinding>("wechatBinding").get<String>("openId"), openId)
    }
}