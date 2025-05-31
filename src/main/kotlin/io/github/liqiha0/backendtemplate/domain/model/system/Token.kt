package io.github.liqiha0.backendtemplate.domain.model.system

import io.github.liqiha0.backendtemplate.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

@Entity
class Token(
    val userId: UUID,
    @Id val accessToken: String,
    val refreshToken: String? = null,
) : AuditableAggregateRoot<Token>() {
    init {
        check(this.accessToken.isNotBlank())
        check(this.refreshToken?.isNotBlank() ?: true)
    }
}

interface TokenRepository : JpaRepository<Token, UUID>, JpaSpecificationExecutor<Token>

fun accessTokenEqual(accessToken: String): Specification<Token> = Specification<Token> { root, _, criteriaBuilder ->
    criteriaBuilder.equal(root.get<String>("accessToken"), accessToken)
}

fun userIdEqual(userId: UUID): Specification<Token> = Specification<Token> { root, _: Any?, criteriaBuilder ->
    criteriaBuilder.equal(root.get<UUID>("userId"), userId)
}
