package io.github.liqiha0.template.storage.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "aliyun.oss")
data class AliyunOssProperties(
    var endpoint: String? = null,
    var accessKeyId: String? = null,
    var accessKeySecret: String? = null,
    var bucketName: String? = null,
    var publicDomain: String? = null,
    var presignedUrlExpirationMinutes: Long = 5
)