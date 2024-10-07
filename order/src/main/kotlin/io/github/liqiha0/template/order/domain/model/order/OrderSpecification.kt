package io.github.liqiha0.template.order.domain.model.order

import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object OrderSpecification {
    fun idEqual(id: String): Specification<Order<*>> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<String>("id"), id)
        }
    }

    fun principalIdEqual(principalId: UUID): Specification<Order<*>> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<UUID>("principalId"), principalId)
        }
    }
}
