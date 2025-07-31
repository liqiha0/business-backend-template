package io.github.liqiha0.template.core.domain.model.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserSelectedStoreRepository : JpaRepository<UserSelectedStore, UUID> {
    fun findByPrincipalId(principalId: UUID): UserSelectedStore?
}
