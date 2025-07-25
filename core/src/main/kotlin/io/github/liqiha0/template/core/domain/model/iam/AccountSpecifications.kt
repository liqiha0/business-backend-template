package io.github.liqiha0.template.core.domain.model.iam

import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

object AccountSpecifications {

    fun hasUsername(username: String): Specification<Account> {
        return Specification { root, _, cb ->
            val credentialJoin = root.join<Account, Credential>("credentials", JoinType.INNER)
            cb.equal(credentialJoin.get<String>("username"), username)
        }
    }

    fun hasOpenId(openId: String): Specification<Account> {
        return Specification { root, _, cb ->
            val credentialJoin = root.join<Account, Credential>("credentials", JoinType.INNER)
            cb.equal(credentialJoin.get<String>("openId"), openId)
        }
    }

    fun hasPhone(phone: String): Specification<Account> {
        return Specification { root, _, cb ->
            val credentialJoin = root.join<Account, Credential>("credentials", JoinType.INNER)
            cb.equal(credentialJoin.get<String>("phone"), phone)
        }
    }
}
