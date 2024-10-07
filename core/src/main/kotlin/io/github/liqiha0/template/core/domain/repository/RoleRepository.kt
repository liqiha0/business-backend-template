package io.github.liqiha0.template.core.domain.repository

import io.github.liqiha0.template.core.domain.model.iam.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface RoleRepository : JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role>