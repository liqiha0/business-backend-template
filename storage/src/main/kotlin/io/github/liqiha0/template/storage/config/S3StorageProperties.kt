package io.github.liqiha0.template.storage.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "storage.s3")
data class S3StorageProperties(
    val bucket: String,
    val region: String,
    val accessKeyId: String? = null,
    val secretAccessKey: String? = null,
    val sessionToken: String? = null,
    val endpoint: String,
    val publicDomain: String? = null,
    val presignExpiration: Duration = Duration.ofMinutes(10),
    val pathStyleAccess: Boolean = false,
)
