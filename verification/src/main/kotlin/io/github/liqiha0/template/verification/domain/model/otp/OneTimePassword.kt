package io.github.liqiha0.template.verification.domain.model.otp

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import org.hibernate.annotations.UuidGenerator
import java.time.ZonedDateTime
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class OneTimePassword(
    var scene: String,
    val code: String = (100000..999999).random().toString(),
    val expiredAt: ZonedDateTime = ZonedDateTime.now().plusMinutes(5),
) : AuditableAggregateRoot<OneTimePassword>() {

    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set

    val expired get() = ZonedDateTime.now().isBefore(expiredAt)
}
