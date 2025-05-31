package io.github.liqiha0.backendtemplate.infrastructure.config

import io.github.liqiha0.backendtemplate.application.system.FileStorage
import io.github.liqiha0.backendtemplate.infrastructure.filestorage.LocalFileStorage
import io.github.liqiha0.backendtemplate.infrastructure.filestorage.PUBLIC_ENDPOINT
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import java.nio.file.Path

@Configuration
@EnableConfigurationProperties(FileStorageProperty::class)
class FileStorageConfig {
    @Bean
    @ConditionalOnProperty(prefix = "file-storage", name = ["backend"], havingValue = "LOCAL")
    fun localFileStorageService(fileStorageProperty: FileStorageProperty): FileStorage {
        val local = fileStorageProperty.local!!
        return LocalFileStorage(local.path, local.publicBaseUrl)
    }

    @Bean
    fun fileStorageFilterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        http.securityMatcher(PUBLIC_ENDPOINT)
        http.authorizeHttpRequests {
            it.anyRequest().permitAll()
        }
        return http.build()
    }
}

@ConfigurationProperties(prefix = "file-storage")
data class FileStorageProperty(
    val backend: FileStorageBackend = FileStorageBackend.NONE,
    val local: LocalFileStorageProperty? = null
)

class LocalFileStorageProperty(val publicBaseUrl: String, val path: Path)

enum class FileStorageBackend {
    NONE,
    LOCAL
}