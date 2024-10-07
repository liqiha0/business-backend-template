package io.github.liqiha0.template.notification.infrastructure.config

data class UCloudSmsProperties(
    val publicKey: String,
    val privateKey: String,
    val projectId: String,
    val region: String,
    val sigContent: List<String>
)
