package io.github.liqiha0.template.core.infrastructure.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SuperuserProperties::class)
class CoreConfiguration
