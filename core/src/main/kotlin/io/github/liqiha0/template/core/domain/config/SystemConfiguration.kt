package io.github.liqiha0.template.core.domain.config

import com.fasterxml.jackson.databind.JsonNode
import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.io.Serializable

@Schema(description = "系统配置")
@Entity
@IdClass(SystemConfigurationId::class)
class SystemConfiguration(
    @Id
    @Schema(description = "配置键组（枚举类名）")
    val keyGroup: String,

    @Id
    @Schema(description = "配置键名（枚举值）")
    val keyName: String,

    @Schema(description = "配置值")
    @Type(JsonType::class)
    var value: JsonNode
) : AuditableAggregateRoot<SystemConfiguration>()

data class SystemConfigurationId(
    val keyGroup: String,
    val keyName: String
) : Serializable

interface SystemConfigurationRepository : JpaRepository<SystemConfiguration, SystemConfigurationId>,
    JpaSpecificationExecutor<SystemConfiguration>
