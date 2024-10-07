package io.github.liqiha0.template.storage.s3

import io.github.liqiha0.template.storage.config.S3StorageProperties
import org.apache.tika.Tika
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.InputStream
import java.net.URI
import java.nio.file.Path
import java.util.UUID

class S3StorageService(
    private val properties: S3StorageProperties,
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
) {

    private val tika = Tika()

    fun getPublicUrl(path: Path): String {
        val key = path.toS3Key()
        properties.publicDomain?.takeIf { it.isNotBlank() }?.let {
            return buildPublicUrl(it, key)
        }

        return runCatching {
            s3Client.utilities().getUrl { builder ->
                builder.bucket(properties.bucket).key(key)
            }.toExternalForm()
        }.getOrElse {
            inferPublicUrl(key)
        }
    }

    fun read(path: Path): InputStream {
        val key = path.toS3Key()
        val request = GetObjectRequest.builder()
            .bucket(properties.bucket)
            .key(key)
            .build()
        return s3Client.getObject(request)
    }

    fun save(
        inputStream: InputStream,
        contentLength: Long? = null,
        subDir: Path? = null,
        originalName: String? = null,
        privateObject: Boolean = true,
    ): Path {
        val objectKey = generateObjectKey(subDir, originalName)
        val buffered = inputStream.buffered()
        buffered.mark(Int.MAX_VALUE)
        val detectedContentType = tika.detect(buffered)
        buffered.reset()

        val requestBody = if (contentLength != null) {
            RequestBody.fromInputStream(buffered, contentLength)
        } else {
            val bytes = buffered.readBytes()
            RequestBody.fromBytes(bytes)
        }

        val putObjectRequestBuilder = PutObjectRequest.builder()
            .bucket(properties.bucket)
            .key(objectKey)
            .contentType(detectedContentType)

        if (!privateObject) {
            putObjectRequestBuilder.acl(ObjectCannedACL.PUBLIC_READ)
        }

        try {
            s3Client.putObject(putObjectRequestBuilder.build(), requestBody)
        } finally {
            buffered.close()
        }
        return Path.of(objectKey)
    }

    fun generateUploadUrl(
        fileName: String,
        subDir: Path? = null,
        contentType: String? = null,
        privateObject: Boolean = true,
    ): String {
        val objectKey = generateObjectKey(subDir, fileName)
        val putObjectRequestBuilder = PutObjectRequest.builder()
            .bucket(properties.bucket)
            .key(objectKey)

        contentType?.takeIf { it.isNotBlank() }?.let { putObjectRequestBuilder.contentType(it) }

        if (!privateObject) {
            putObjectRequestBuilder.acl(ObjectCannedACL.PUBLIC_READ)
        }

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(properties.presignExpiration)
            .putObjectRequest(putObjectRequestBuilder.build())
            .build()

        val presignedRequest = s3Presigner.presignPutObject(presignRequest)
        return presignedRequest.url().toExternalForm()
    }

    private fun generateObjectKey(subDir: Path?, originalName: String?): String {
        val uuid = UUID.randomUUID().toString().replace("-", "")
        val extension = originalName?.substringAfterLast('.', "")?.takeIf { it.isNotBlank() }
        val fileName = if (extension != null) "$uuid.$extension" else uuid
        return subDir?.let { buildKey(it, fileName) } ?: fileName
    }

    private fun buildKey(subDir: Path, fileName: String): String {
        val sanitizedDir = subDir.toString()
            .replace('\\', '/')
            .trim('/')
        return if (sanitizedDir.isEmpty()) fileName else "$sanitizedDir/$fileName"
    }

    private fun Path.toS3Key(): String = this.toString().replace('\\', '/')

    private fun buildPublicUrl(publicDomain: String, key: String): String {
        val base = publicDomain.trimEnd('/')
        return "$base/$key"
    }

    private fun inferPublicUrl(objectKey: String): String {
        val endpoint = properties.endpoint.trim()
        val endpointUri = runCatching { URI.create(endpoint) }.getOrNull()

        if (properties.pathStyleAccess || endpointUri == null || endpointUri.authority.isNullOrBlank()) {
            val base = endpoint.trimEnd('/')
            return "$base/${properties.bucket}/$objectKey"
        }

        val authority = endpointUri.authority ?: endpointUri.host
        val scheme = endpointUri.scheme ?: "https"
        return "$scheme://${properties.bucket}.${authority.trim('/')}/$objectKey"
    }
}
