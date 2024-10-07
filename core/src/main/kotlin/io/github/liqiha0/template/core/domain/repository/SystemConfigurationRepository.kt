package io.github.liqiha0.template.core.domain.repository

import io.github.liqiha0.template.core.domain.model.config.SystemConfiguration
import io.github.liqiha0.template.core.domain.model.config.SystemConfigurationId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface SystemConfigurationRepository : JpaRepository<SystemConfiguration, SystemConfigurationId>,
    JpaSpecificationExecutor<SystemConfiguration>