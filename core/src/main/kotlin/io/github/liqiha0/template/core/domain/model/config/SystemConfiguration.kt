package io.github.liqiha0.template.core.domain.model.config

import com.fasterxml.jackson.databind.JsonNode
import io.github.liqiha0.template.core.domain.config.ConfigurationKey
import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import org.hibernate.annotations.Type
import java.io.Serializable

@Schema(description = "系统配置")
@Entity
class SystemConfiguration(
    @EmbeddedId
    val id: SystemConfigurationId,

    @Schema(description = "配置值")
    @Type(JsonType::class)
    @Column(columnDefinition = "jsonb", nullable = false)
    var value: JsonNode
) : AuditableAggregateRoot<SystemConfiguration>() {
    constructor(key: ConfigurationKey<*>, value: JsonNode) : this(SystemConfigurationId(key), value)
}

@Embeddable
data class SystemConfigurationId(
    @Schema(description = "配置键组")
    @Column(nullable = false)
    val keyGroup: String,
    @Schema(description = "配置键名")
    @Column(nullable = false)
    val keyName: String
) : Serializable {
    constructor(key: ConfigurationKey<*>) : this(key.keyGroup, key.keyName)
}

