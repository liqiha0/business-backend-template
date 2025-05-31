package io.github.liqiha0.backendtemplate.domain.model.system.smscode

import io.github.liqiha0.backendtemplate.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*

enum class SmsCodeType {
    LOGIN
}

@Entity
class SmsCode(
    var phone: String,
    @Enumerated(EnumType.STRING)
    var type: SmsCodeType,
) : AuditableAggregateRoot<SmsCode>() {

    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set

    val code: String = (100000..999999).random().toString()
    val expiredAt: ZonedDateTime = ZonedDateTime.now().plusMinutes(5)

    fun isExpired(): Boolean {
        return ZonedDateTime.now().isAfter(expiredAt)
    }
}

@Repository
interface SmsCodeRepository : JpaRepository<SmsCode, UUID> {
    fun findFirstByPhoneAndTypeOrderByCreatedDateDesc(phone: String, type: SmsCodeType): SmsCode?
}
