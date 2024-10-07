package io.github.liqiha0.template.core.application

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.liqiha0.template.core.domain.config.ConfigurationKey
import io.github.liqiha0.template.core.domain.model.config.SystemConfiguration
import io.github.liqiha0.template.core.domain.model.config.SystemConfigurationId
import io.github.liqiha0.template.core.domain.repository.SystemConfigurationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SystemConfigurationService(
    private val repository: SystemConfigurationRepository,
    private val objectMapper: ObjectMapper,
) {

    @Transactional(readOnly = true)
    fun <T> getValue(key: ConfigurationKey<T>): T? {
        val id = SystemConfigurationId(key)
        val fromDb = repository.findById(id).map { it.value }

        if (fromDb.isPresent) {
            val jsonNode = fromDb.get()
            if (jsonNode.isNull) {
                return null
            }
            return objectMapper.treeToValue(jsonNode, key.valueType)
        }

        return key.defaultValue
    }

    @Transactional
    fun <T> getValueOrDefault(key: ConfigurationKey<T>): T {
        return this.getValue(key) ?: error("Default value is null for key ${key.keyGroup}.${key.keyName}")
    }

    @Transactional
    fun <T> setValue(key: ConfigurationKey<T>, value: T?) {
        val id = SystemConfigurationId(key)
        val jsonValue: JsonNode = objectMapper.valueToTree(value)
        val config = repository.findById(id).orElseGet {
            SystemConfiguration(key, jsonValue)
        }
        config.value = jsonValue
        repository.save(config)
    }
}