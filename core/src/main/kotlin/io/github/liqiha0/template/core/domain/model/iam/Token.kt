package io.github.liqiha0.template.core.domain.model.iam

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.github.liqiha0.template.core.domain.shared.NonBlankString
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class Token(
    @Column(nullable = false)
    val principalId: UUID,
    @Id
    @Column(nullable = false)
    val accessToken: NonBlankString,
) : AuditableAggregateRoot<Token>() {
}
