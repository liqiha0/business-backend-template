package io.github.liqiha0.template.core.domain.specification

import io.github.liqiha0.template.core.domain.model.iam.*
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import kotlin.reflect.KClass

object PrincipalSpecification {

    fun <T : Identity> hasIdentity(
        type: Class<T>,
        field: String,
        value: Any,
        realm: String? = null
    ): Specification<Principal> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            predicates += if (realm == null) {
                cb.isNull(root.get<String>("realm"))
            } else {
                cb.equal(root.get<String>("realm"), realm)
            }

            val identityJoin = root.join<Principal, Identity>("_identities", JoinType.INNER)
            predicates += cb.equal(identityJoin.get<Any>(field), value)

            cb.and(*predicates.toTypedArray())
        }
    }

    fun <T : Identity> hasIdentity(
        type: KClass<T>,
        field: String,
        value: Any,
        realm: String? = null
    ): Specification<Principal> = hasIdentity(type.java, field, value, realm)

    fun hasUsername(username: String): Specification<Principal> =
        hasIdentity(UsernameIdentity::class, "username", username)

    fun hasOpenId(openId: String): Specification<Principal> =
        hasIdentity(WechatIdentity::class, "openId", openId)

    fun hasPhone(phone: String): Specification<Principal> =
        hasIdentity(PhoneIdentity::class, "phone", phone)

    fun hasWechatUnionId(unionId: String): Specification<Principal> =
        hasIdentity(WechatIdentity::class, "unionId", unionId)
}
