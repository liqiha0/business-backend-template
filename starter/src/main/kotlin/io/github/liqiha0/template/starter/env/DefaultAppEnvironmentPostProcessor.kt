package io.github.liqiha0.template.starter.env

import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

class DefaultAppEnvironmentPostProcessor : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        val defaults = mapOf(
            "spring.datasource.url" to "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_UPPER=false",

            "spring.jpa.properties.hibernate.globally_quoted_identifiers" to "true",

            "spring.data.web.pageable.one-indexed-parameters" to "true",
            "spring.data.web.pageable.serialization-mode" to "via_dto",

            "spring.threads.virtual.enabled" to "true",

            "spring.flyway.baseline-on-migrate" to "true",
            "spring.flyway.baseline-version" to "0",
        )

        val source = MapPropertySource("app-starter-defaults", defaults)
        environment.propertySources.addLast(source)
    }
}

