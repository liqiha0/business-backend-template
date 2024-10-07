package io.github.liqiha0.template.core.domain.repository

import io.github.liqiha0.template.core.domain.model.iam.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface TokenRepository : JpaRepository<Token, String>, JpaSpecificationExecutor<Token>