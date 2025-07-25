package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.config.SystemConfiguration
import io.github.liqiha0.template.core.domain.config.SystemConfigurationId
import io.github.liqiha0.template.core.domain.config.SystemConfigurationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SystemConfigurationService(
    private val repository: SystemConfigurationRepository
) {

    @Transactional(readOnly = true)
    fun getValue(key: Enum<*>, defaultValue: String? = null): String? {
        val id = SystemConfigurationId(key.javaClass.name, key.name)
        val fromDb = repository.findById(id).map { it.value }
        if (fromDb.isPresent) {
            return fromDb.get()
        }
        if (defaultValue != null) {
            return defaultValue
        }
        if (key is io.github.liqiha0.template.core.domain.config.ConfigurationEnum) {
            return key.defaultValue
        }
        return null
    }

    @Transactional
    fun setValue(key: Enum<*>, value: String) {
        val id = SystemConfigurationId(key.javaClass.name, key.name)
        val config = repository.findById(id).orElseGet {
            SystemConfiguration(keyGroup = id.keyGroup, keyName = id.keyName, value = value)
        }
        config.value = value
        repository.save(config)
    }
}
