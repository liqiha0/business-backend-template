plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
}

group = "io.github.liqiha0.template"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(platform(libs.spring.boot.bom))

    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.test {
    useJUnitPlatform()
}
