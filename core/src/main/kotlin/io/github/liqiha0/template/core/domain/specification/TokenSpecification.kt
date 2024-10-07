package io.github.liqiha0.template.core.domain.specification

import io.github.liqiha0.template.core.domain.model.iam.Token
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object TokenSpecification {
    fun accessTokenEqual(accessToken: String): Specification<Token> = Specification<Token> { root, _, criteriaBuilder ->
        criteriaBuilder.equal(root.get<String>("accessToken"), accessToken)
    }

    fun principalIdEqual(userId: UUID): Specification<Token> = Specification<Token> { root, _: Any?, criteriaBuilder ->
        criteriaBuilder.equal(root.get<UUID>("principalId"), userId)
    }
}