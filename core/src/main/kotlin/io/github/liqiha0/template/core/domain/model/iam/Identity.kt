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

    abstract var realm: String?
}

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["realm", "username"])])
class UsernameIdentity(
    @Column(nullable = false)
    val username: String,
) : Identity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UsernameIdentity) return false
        return username == other.username
    }

    override fun hashCode(): Int = username.hashCode()
    override var realm: String? = null
}

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["realm", "phone"])])
class PhoneIdentity(
    val phone: String,
) : Identity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PhoneIdentity) return false
        return phone == other.phone
    }

    override fun hashCode(): Int = phone.hashCode()
    override var realm: String? = null
}

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["realm", "openId"])])
class WechatIdentity(
    @Column(nullable = false)
    val openId: String,
    @Column(nullable = true)
    var unionId: String? = null,
    @Column(nullable = true)
    var appId: String? = null,
) : Identity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WechatIdentity) return false
        return openId == other.openId &&
                unionId == other.unionId &&
                appId == other.appId
    }

    override fun hashCode(): Int {
        var result = openId.hashCode()
        result = 31 * result + (unionId?.hashCode() ?: 0)
        result = 31 * result + (appId?.hashCode() ?: 0)
        return result
    }

    override var realm: String? = null
}
