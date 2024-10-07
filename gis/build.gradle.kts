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
    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter")

    api("org.locationtech.jts:jts-core:1.20.0")
    api("org.hibernate.orm:hibernate-spatial")
    api("org.n52.jackson:jackson-datatype-jts:2.0.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}