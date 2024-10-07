package io.github.liqiha0.backendtemplate.domain.model.system

import io.github.liqiha0.backendtemplate.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@Entity
class Role(
    displayName: String,
    @Type(JsonType::class)
    @Schema(description = "权限", example = "[\"ADMINISTRATOR\"]")
    var authority: Set<String>,
) : AuditableAggregateRoot<Role>() {

    init {
        check(displayName.isNotBlank())
    }

    @Schema(description = "名称")
    var displayName = displayName
        set(value) {
            check(this.displayName.isNotBlank())
            field = value
        }

    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set
}

interface RoleRepository : JpaRepository<Role, UUID>