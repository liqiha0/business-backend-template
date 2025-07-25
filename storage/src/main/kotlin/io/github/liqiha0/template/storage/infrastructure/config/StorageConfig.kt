package io.github.liqiha0.template.storage.infrastructure.config

import io.github.liqiha0.template.storage.application.Storage
import io.github.liqiha0.template.storage.infrastructure.filestorage.LocalStorage
import io.github.liqiha0.template.storage.infrastructure.filestorage.PUBLIC_ENDPOINT
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import java.nio.file.Path

@Configuration
@EnableConfigurationProperties(StorageProperty::class)
class StorageConfig {
    @Bean
    @ConditionalOnProperty(prefix = "storage", name = ["backend"], havingValue = "LOCAL")
    fun localFileStorageService(storageProperty: StorageProperty): Storage {
        val local = storageProperty.local!!
        return LocalStorage(local.path, local.publicBaseUrl)
    }

    @Bean
    // TODO: 尝试移除顺序配置
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun fileStorageFilterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        http.securityMatcher(PUBLIC_ENDPOINT)
        http.authorizeHttpRequests {
            it.anyRequest().permitAll()
        }
        return http.build()
    }
}

@ConfigurationProperties(prefix = "storage")
data class StorageProperty(
    val backend: StorageBackend = StorageBackend.NONE,
    val local: LocalStorageProperty? = null
)

class LocalStorageProperty(val publicBaseUrl: String, val path: Path)

enum class StorageBackend {
    NONE,
    LOCAL
}