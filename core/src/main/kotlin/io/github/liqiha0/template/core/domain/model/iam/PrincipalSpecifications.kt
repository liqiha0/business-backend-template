package io.github.liqiha0.template.core.domain.model.iam

import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

object PrincipalSpecifications {

    fun hasUsername(username: String): Specification<Principal> {
        return Specification { root, _, cb ->
            val identityJoin = root.join<Principal, Identity>("identities", JoinType.INNER)
            val usernameIdentity = cb.treat(identityJoin, UsernameIdentity::class.java)
            cb.equal(usernameIdentity.get<String>("username"), username)
        }
    }

    fun hasOpenId(openId: String): Specification<Principal> {
        return Specification { root, _, cb ->
            val identityJoin = root.join<Principal, Identity>("identities", JoinType.INNER)
            val wechatIdentity = cb.treat(identityJoin, WechatIdentity::class.java)
            cb.equal(wechatIdentity.get<String>("openId"), openId)
        }
    }

    fun hasPhone(phone: String): Specification<Principal> {
        return Specification { root, _, cb ->
            val identityJoin = root.join<Principal, Identity>("identities", JoinType.INNER)
            val phoneIdentity = cb.treat(identityJoin, PhoneIdentity::class.java)
            cb.equal(phoneIdentity.get<String>("phone"), phone)
        }
    }
}
