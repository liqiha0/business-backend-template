package io.github.liqiha0.template.storage.config

import io.github.liqiha0.template.storage.s3.S3StorageService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
@EnableConfigurationProperties(S3StorageProperties::class)
class S3StorageConfig {

    @Bean
    fun s3CredentialsProvider(properties: S3StorageProperties): AwsCredentialsProvider {
        val accessKeyId = properties.accessKeyId
        val secretAccessKey = properties.secretAccessKey
        if (accessKeyId.isNullOrBlank() || secretAccessKey.isNullOrBlank()) {
            return DefaultCredentialsProvider.create()
        }

        val sessionToken = properties.sessionToken
        val credentials = if (sessionToken.isNullOrBlank()) {
            AwsBasicCredentials.create(accessKeyId, secretAccessKey)
        } else {
            AwsSessionCredentials.create(accessKeyId, secretAccessKey, sessionToken)
        }
        return StaticCredentialsProvider.create(credentials)
    }

    @Bean
    fun s3Client(
        properties: S3StorageProperties,
        credentialsProvider: AwsCredentialsProvider,
    ): S3Client {
        val endpoint = properties.endpoint.trim()
        require(endpoint.isNotEmpty()) { "storage.s3.endpoint must not be blank" }

        val builder = S3Client.builder()
            .region(Region.of(properties.region))
            .credentialsProvider(credentialsProvider)

        if (properties.pathStyleAccess) {
            builder.serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build()
            )
        }

        builder.endpointOverride(URI.create(endpoint))

        return builder.build()
    }

    @Bean
    fun s3Presigner(
        properties: S3StorageProperties,
        credentialsProvider: AwsCredentialsProvider,
    ): S3Presigner {
        val endpoint = properties.endpoint.trim()
        require(endpoint.isNotEmpty()) { "storage.s3.endpoint must not be blank" }

        val builder = S3Presigner.builder()
            .region(Region.of(properties.region))
            .credentialsProvider(credentialsProvider)

        builder.endpointOverride(URI.create(endpoint))

        return builder.build()
    }

    @Bean
    fun s3StorageService(
        properties: S3StorageProperties,
        s3Client: S3Client,
        s3Presigner: S3Presigner,
    ): S3StorageService {
        return S3StorageService(
            properties = properties,
            s3Client = s3Client,
            s3Presigner = s3Presigner,
        )
    }
}
