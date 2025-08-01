package io.github.liqiha0.template.core.domain.model.iam

import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

object PrincipalSpecifications {

    fun hasUsername(username: String): Specification<Principal> {
        return Specification { root, _, cb ->
            val identityJoin = root.join<Principal, Identity>("identities", JoinType.INNER)
            cb.equal(identityJoin.get<String>("username"), username)
        }
    }

    fun hasOpenId(openId: String): Specification<Principal> {
        return Specification { root, _, cb ->
            val identityJoin = root.join<Principal, Identity>("identities", JoinType.INNER)
            cb.equal(identityJoin.get<String>("openId"), openId)
        }
    }

    fun hasPhone(phone: String): Specification<Principal> {
        return Specification { root, _, cb ->
            val identityJoin = root.join<Principal, Identity>("identities", JoinType.INNER)
            cb.equal(identityJoin.get<String>("phone"), phone)
        }
    }
}
