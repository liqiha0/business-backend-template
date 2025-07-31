package io.github.liqiha0.template.core.application

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.liqiha0.template.core.domain.config.ConfigurationEnum
import io.github.liqiha0.template.core.domain.config.SystemConfiguration
import io.github.liqiha0.template.core.domain.config.SystemConfigurationId
import io.github.liqiha0.template.core.domain.config.SystemConfigurationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SystemConfigurationService(
    private val repository: SystemConfigurationRepository,
    private val objectMapper: ObjectMapper,
) {

    @Transactional(readOnly = true)
    fun <T> getValue(key: ConfigurationEnum<*, T>): T? {
        val id = SystemConfigurationId(key.javaClass.name, (key as Enum<*>).name)
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
    fun setValue(key: Enum<*>, value: Any?) {
        val id = SystemConfigurationId(key.javaClass.name, key.name)
        val jsonValue: JsonNode = objectMapper.valueToTree(value)
        val config = repository.findById(id).orElseGet {
            SystemConfiguration(keyGroup = id.keyGroup, keyName = id.keyName, value = jsonValue)
        }
        config.value = jsonValue
        repository.save(config)
    }
}