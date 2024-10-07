plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

group = "io.github.liqiha0.template"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(platform(libs.spring.boot.bom))

    api(project(":core"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-web")

    api("org.flywaydb:flyway-core")
    api("org.flywaydb:flyway-database-postgresql")
}
