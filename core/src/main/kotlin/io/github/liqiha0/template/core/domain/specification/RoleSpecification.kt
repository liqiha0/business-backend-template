package io.github.liqiha0.template.core.domain.specification

import io.github.liqiha0.template.core.domain.model.iam.Role
import org.springframework.data.jpa.domain.Specification

object RoleSpecification {
    fun displayNameLike(displayName: String): Specification<Role> {
        return Specification<Role> { root, query, criteriaBuilder ->
            criteriaBuilder.like(root.get("displayName"), displayName)
        }
    }
}