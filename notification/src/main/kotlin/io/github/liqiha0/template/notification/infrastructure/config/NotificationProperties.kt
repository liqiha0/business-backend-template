package io.github.liqiha0.template.notification.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("notification")
data class NotificationProperties(
    @NestedConfigurationProperty
    val sms: SmsProperties
)

data class SmsProperties(
    val provider: SmsProviderType? = null,
    @NestedConfigurationProperty
    val ucloud: UCloudSmsProperties?
)
