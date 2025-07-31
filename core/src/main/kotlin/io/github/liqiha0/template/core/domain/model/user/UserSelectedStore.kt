package io.github.liqiha0.template.core.domain.model.user

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
open class UserSelectedStore(
    @Column(unique = true, nullable = false)
    var principalId: UUID,
    var storeId: UUID // 不可为空
) : AuditableAggregateRoot<UserSelectedStore>() {
    @Id
    @UuidGenerator
    lateinit var id: UUID
        protected set
}
