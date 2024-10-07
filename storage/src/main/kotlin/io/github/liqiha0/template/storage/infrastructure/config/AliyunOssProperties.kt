package io.github.liqiha0.template.storage.infrastructure.config

data class AliyunOssProperties(
    var endpoint: String? = null,
    var accessKeyId: String? = null,
    var accessKeySecret: String? = null,
    var bucketName: String? = null,
    var publicDomain: String? = null,
    var presignedUrlExpirationMinutes: Long = 5
)