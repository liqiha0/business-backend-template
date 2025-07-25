package io.github.liqiha0.template.core.domain.model.iam

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

@Entity
class Role(
    displayName: String,
    @Type(JsonType::class)
    @Schema(description = "权限", example = "[\"ADMINISTRATOR\"]")
    var authority: Set<String>,
    @Schema(description = "是否为内置角色")
    val isBuiltin: Boolean = false
) : AuditableAggregateRoot<Role>() {

    init {
        require(displayName.isNotBlank())
    }

    @Schema(description = "名称")
    var displayName = displayName
        set(value) {
            require(this.displayName.isNotBlank())
            field = value
        }

    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set
}

interface RoleRepository : JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role>

fun displayNameEqual(displayName: String): Specification<Role> {
    return Specification<Role> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<String>("displayName"), displayName)
    }
}

fun displayNameLike(displayName: String): Specification<Role> {
    return Specification<Role> { root, query, criteriaBuilder ->
        criteriaBuilder.like(root.get("displayName"), displayName)
    }
}

