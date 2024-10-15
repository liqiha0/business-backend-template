package io.github.liqiha0.backendtemplate.domain.model.system

import com.fasterxml.jackson.annotation.JsonIgnore
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import org.hibernate.annotations.Type
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

const val DEFAULT_ADMIN_USERNAME = "admin"
const val DEFAULT_ADMIN_PASSWORD = "admin123"

@Entity
class AdminAccount(
    displayName: String,
    @Schema(description = "用户名")
    val username: String,
    password: String,
    @Type(JsonType::class)
    var roleIds: Set<UUID> = emptySet(),
) : Account<AdminAccount>(displayName) {

    init {
        check(this.username.isNotBlank())
        check(password.isNotBlank())
    }

    @Schema(description = "密码")
    @JsonIgnore
    var password: String = password
        set(value) {
            check(value.isNotBlank())
            field = value
        }
}

interface AdminAccountRepository : JpaRepository<AdminAccount, UUID>, JpaSpecificationExecutor<AdminAccount> {
    fun findByUsername(username: String): AdminAccount?
}

fun usernameEqual(username: String?): Specification<AdminAccount> {
    return Specification<AdminAccount> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<String>("username"), username)
    }
}