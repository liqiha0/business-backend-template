package io.github.liqiha0.template.verification.domain.model.otp

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Column
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
    @Column(nullable = false)
    val type: OtpType,
    @Column(nullable = false)
    val scene: String,
    @Column(nullable = false)
    val recipient: String,
    @Column(nullable = false)
    val code: String = (100000..999999).random().toString(),
    @Column(nullable = false)
    val expiredAt: ZonedDateTime = ZonedDateTime.now().plusMinutes(5)
) : AuditableAggregateRoot<OneTimePassword>() {

    @Id
    @UuidGenerator
    @Column(nullable = false)
    lateinit var id: UUID
        protected set

    fun isStillValid(): Boolean {
        return ZonedDateTime.now().isBefore(expiredAt)
    }
}
