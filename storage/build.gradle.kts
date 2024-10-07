plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

group = "io.github.liqiha0.template"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(platform("software.amazon.awssdk:bom:2.29.28"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation(libs.apache.tika.core)
    implementation("software.amazon.awssdk:s3")
}

tasks.test {
    useJUnitPlatform()
}
