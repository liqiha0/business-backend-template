package io.github.liqiha0.template.core.domain.model.iam

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
open class Role(
    displayName: String,
    @Type(JsonType::class)
    @Schema(description = "权限", example = "[\"ADMINISTRATOR\"]")
    @Column(columnDefinition = "jsonb", nullable = false)
    var authority: Set<String>,
    @Schema(description = "是否为内置角色")
    @Column(nullable = false)
    val isBuiltin: Boolean = false
) : AuditableAggregateRoot<Role>() {

    init {
        require(displayName.isNotBlank())
    }

    @Schema(description = "名称")
    @Column(nullable = false)
    var displayName = displayName
        set(value) {
            require(value.isNotBlank())
            field = value
        }

    @Id
    @UuidGenerator
    @Column(nullable = false)
    lateinit var id: UUID
        protected set
}

