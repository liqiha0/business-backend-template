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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("org.flywaydb:flyway-core")
    api("org.flywaydb:flyway-database-postgresql")

    api("org.locationtech.jts:jts-core:1.20.0")
    implementation("org.hibernate.orm:hibernate-spatial")
    api("io.hypersistence:hypersistence-utils-hibernate-63:3.8.1")
    implementation("org.n52.jackson:jackson-datatype-jts:2.0.0")

    api("org.springdoc:springdoc-openapi-starter-webmvc-api:2.5.0")

    compileOnly("com.github.binarywang:wx-java-miniapp-multi-spring-boot-starter:4.7.0")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
