plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "io.github.liqiha0.template"
version = "0.0.1-SNAPSHOT"

dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.flywaydb:flyway-core")
    api("org.flywaydb:flyway-database-postgresql")

    api("io.hypersistence:hypersistence-utils-hibernate-63:3.8.1")
    api("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.9")

    compileOnly("com.github.binarywang:wx-java-miniapp-multi-spring-boot-starter:4.7.0")
    compileOnly("com.github.binarywang:wx-java-mp-spring-boot-starter:4.7.0")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
