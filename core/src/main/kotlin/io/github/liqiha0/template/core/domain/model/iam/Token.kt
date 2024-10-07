package io.github.liqiha0.template.core.domain.model.iam

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
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
    val accessToken: String,
    @Column(nullable = true)
    val refreshToken: String? = null,
) : AuditableAggregateRoot<Token>() {
    init {
        require(this.accessToken.isNotBlank())
        require(this.refreshToken?.isNotBlank() ?: true)
    }
}
