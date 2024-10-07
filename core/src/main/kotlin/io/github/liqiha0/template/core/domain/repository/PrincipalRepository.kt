package io.github.liqiha0.template.core.domain.repository

import io.github.liqiha0.template.core.domain.model.iam.Principal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface PrincipalRepository : JpaRepository<Principal, UUID>, JpaSpecificationExecutor<Principal>