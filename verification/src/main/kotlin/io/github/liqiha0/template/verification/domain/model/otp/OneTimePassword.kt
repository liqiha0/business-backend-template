package io.github.liqiha0.template.verification.domain.model.otp

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import java.time.ZonedDateTime
import java.util.UUID

@Entity
class OneTimePassword(
    @Enumerated(EnumType.STRING)
    val type: OtpType,
    val scene: String,
    val recipient: String,
    val code: String = (100000..999999).random().toString(),
    val expiredAt: ZonedDateTime = ZonedDateTime.now().plusMinutes(5)
) : AuditableAggregateRoot<OneTimePassword>() {

    @Id
    @UuidGenerator
    lateinit var id: UUID
        protected set

    fun isStillValid(): Boolean {
        return ZonedDateTime.now().isBefore(expiredAt)
    }
}