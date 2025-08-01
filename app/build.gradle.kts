import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.hibernate.orm")
    id("org.graalvm.buildtools.native")
}

group = "io.github.liqiha0"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor.set(JvmVendorSpec.GRAAL_VM)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":order"))
    implementation(project(":storage"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.8.1")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.9")

    // 微信相关
    implementation("com.github.binarywang:wx-java-miniapp-multi-spring-boot-starter:4.7.0")
    implementation("com.github.binarywang:wx-java-pay-spring-boot-starter:4.7.0")

    implementation("com.graphhopper.external:jackson-datatype-jts:2.14")
    implementation("org.locationtech.jts:jts-core:1.20.0")
    implementation("org.hibernate.orm:hibernate-spatial")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// TODO:
//hibernate {
//    enhancement {
//        enableAssociationManagement = true
//    }
//}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}