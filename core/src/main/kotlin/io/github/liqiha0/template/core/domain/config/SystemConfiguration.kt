package io.github.liqiha0.template.core.domain.config

import com.fasterxml.jackson.databind.JsonNode
import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.io.Serializable

@Schema(description = "系统配置")
@Entity
class SystemConfiguration(
    @EmbeddedId
    val id: SystemConfigurationId,

    @Schema(description = "配置值")
    @Type(JsonType::class)
    var value: JsonNode
) : AuditableAggregateRoot<SystemConfiguration>() {
    constructor(key: ConfigurationKey<*>, value: JsonNode) : this(SystemConfigurationId(key), value)
}

@Embeddable
data class SystemConfigurationId(
    @Schema(description = "配置键组")
    val keyGroup: String,
    @Schema(description = "配置键名")
    val keyName: String
) : Serializable {
    constructor(key: ConfigurationKey<*>) : this(key.keyGroup, key.keyName)
}

interface SystemConfigurationRepository : JpaRepository<SystemConfiguration, SystemConfigurationId>,
    JpaSpecificationExecutor<SystemConfiguration>
