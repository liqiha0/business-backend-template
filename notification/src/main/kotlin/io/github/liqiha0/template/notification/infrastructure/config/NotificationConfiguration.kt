package io.github.liqiha0.template.notification.infrastructure.config

import io.github.liqiha0.template.notification.domain.service.SmsService
import io.github.liqiha0.template.notification.infrastructure.service.ConsoleLogSmsService
import io.github.liqiha0.template.notification.infrastructure.service.UCloudSmsService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(NotificationProperties::class)
class NotificationConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "notification.sms", name = ["provider"], havingValue = "ucloud")
    fun ucloudSmsService(properties: NotificationProperties): SmsService {
        val ucloudProperties = properties.sms.ucloud ?: throw IllegalStateException("UCloud properties must be set when provider is UCLOUD")
        return UCloudSmsService(ucloudProperties)
    }

    @Bean
    @ConditionalOnProperty(prefix = "notification.sms", name = ["provider"], havingValue = "console_log")
    fun consoleLogSmsService(): SmsService {
        return ConsoleLogSmsService()
    }
}
