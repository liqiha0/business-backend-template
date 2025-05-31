package io.github.liqiha0.backendtemplate.domain.model.system

import io.github.liqiha0.backendtemplate.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Account<T : Account<T>>(
    displayName: String,
    var isDisabled: Boolean = false,
    @Type(JsonType::class)
    var roleIds: Set<UUID> = emptySet()
) : AuditableAggregateRoot<T>() {
    init {
        check(displayName.isNotBlank())
    }

    @Schema(description = "名称")
    var displayName = displayName
        set(value) {
            check(value.isNotBlank())
            field = value
        }

    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set
}

interface AccountRepository<T : Account<*>> : JpaRepository<T, UUID>, JpaSpecificationExecutor<T>

fun <T : Account<T>> displayNameLike(displayName: String): Specification<T> {
    return Specification<T> { root, query, criteriaBuilder ->
        criteriaBuilder.like(root.get<String>("displayName"), displayName)
    }
}