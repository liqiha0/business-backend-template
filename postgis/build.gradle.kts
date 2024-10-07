plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

group = "io.github.liqiha0.template"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(platform(libs.spring.boot.bom))

    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter")

    api(libs.jts.core)
    api("org.hibernate.orm:hibernate-spatial")
    api(libs.jackson.datatype.jts)
}

tasks.test {
    useJUnitPlatform()
}
