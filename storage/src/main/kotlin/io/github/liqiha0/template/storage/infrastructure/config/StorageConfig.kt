package io.github.liqiha0.template.storage.infrastructure.config

import io.github.liqiha0.template.storage.application.Storage
import io.github.liqiha0.template.storage.infrastructure.AliyunOssStorage
import io.github.liqiha0.template.storage.infrastructure.filestorage.LocalStorage
import io.github.liqiha0.template.storage.infrastructure.filestorage.PUBLIC_ENDPOINT
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import java.nio.file.Path

@Configuration
@EnableConfigurationProperties(StorageProperties::class)
class StorageConfig {
    @Bean
    @ConditionalOnProperty(prefix = "storage", name = ["provider"], havingValue = "local")
    fun localStorage(properties: StorageProperties): Storage {
        val localProperties = properties.local ?: throw IllegalStateException("Local storage properties must be set when provider is LOCAL")
        return LocalStorage(localProperties.path, localProperties.publicBaseUrl)
    }

    @Bean
    @ConditionalOnProperty(prefix = "storage", name = ["provider"], havingValue = "aliyun_oss")
    fun aliyunOssStorage(properties: StorageProperties): Storage {
        val ossProperties = properties.aliyunOss ?: throw IllegalStateException("Aliyun OSS properties must be set when provider is ALIYUN_OSS")
        return AliyunOssStorage(
            endpoint = ossProperties.endpoint!!,
            accessKeyId = ossProperties.accessKeyId!!,
            accessKeySecret = ossProperties.accessKeySecret!!,
            bucketName = ossProperties.bucketName!!,
            presignedUrlExpirationMinutes = ossProperties.presignedUrlExpirationMinutes
        )
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun fileStorageFilterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        http.securityMatcher(PUBLIC_ENDPOINT, "/storage/generate-upload-url")
        http.authorizeHttpRequests {
            it.anyRequest().permitAll()
        }
        return http.build()
    }
}

@ConfigurationProperties(prefix = "storage")
data class StorageProperties(
    val provider: StorageProviderType? = null,
    @NestedConfigurationProperty
    val local: LocalStorageProperty? = null,
    @NestedConfigurationProperty
    val aliyunOss: AliyunOssProperties? = null,
    val enableUploadUrlGenerationApi: Boolean = true
)

class LocalStorageProperty(val publicBaseUrl: String, val path: Path)
