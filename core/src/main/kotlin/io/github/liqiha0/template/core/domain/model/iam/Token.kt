package io.github.liqiha0.template.core.domain.model.iam

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

@Entity
class Token(
    val accountId: UUID,
    @Id val accessToken: String,
    val refreshToken: String? = null,
) : AuditableAggregateRoot<Token>() {
    init {
        require(this.accessToken.isNotBlank())
        require(this.refreshToken?.isNotBlank() ?: true)
    }
}

interface TokenRepository : JpaRepository<Token, UUID>, JpaSpecificationExecutor<Token>

fun accessTokenEqual(accessToken: String): Specification<Token> = Specification<Token> { root, _, criteriaBuilder ->
    criteriaBuilder.equal(root.get<String>("accessToken"), accessToken)
}

fun userIdEqual(userId: UUID): Specification<Token> = Specification<Token> { root, _: Any?, criteriaBuilder ->
    criteriaBuilder.equal(root.get<UUID>("userId"), userId)
}
