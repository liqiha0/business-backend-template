package io.github.liqiha0.template.storage.infrastructure

import com.aliyun.oss.ClientBuilderConfiguration
import com.aliyun.oss.HttpMethod
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.common.auth.DefaultCredentialProvider
import com.aliyun.oss.common.comm.Protocol
import com.aliyun.oss.common.comm.SignVersion
import com.aliyun.oss.model.GeneratePresignedUrlRequest
import com.aliyun.oss.model.ObjectMetadata
import io.github.liqiha0.template.storage.application.Storage
import org.apache.tika.Tika
import java.io.InputStream
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit


class AliyunOssStorage(
    private val endpoint: String,
    private val accessKeyId: String,
    private val accessKeySecret: String,
    private val bucketName: String,
    private val presignedUrlExpirationMinutes: Long
) : Storage {

    private val ossClient: OSS by lazy {
        val clientBuilderConfiguration = ClientBuilderConfiguration()
        clientBuilderConfiguration.setProtocol(Protocol.HTTPS)
        OSSClientBuilder.create(
        ).endpoint(endpoint)
            .credentialsProvider(DefaultCredentialProvider(accessKeyId, accessKeySecret))
            .build()
    }

    init {
        println(ossClient.listBuckets())
    }

    private val tika = Tika()

    override fun getPublicUrl(path: Path): String {
        return "https://$bucketName.$endpoint/$path"
    }

    override fun read(path: Path): InputStream {
        val ossObject = ossClient.getObject(bucketName, path.toString())
        return ossObject.objectContent
    }

    override fun save(
        inputStream: InputStream,
        subDir: Path?,
        originalName: String?,
        private: Boolean
    ): Path {
        // Wrap in a BufferedInputStream to allow marking and resetting, which is necessary
        // because Tika consumes the stream to detect the content type.
        val bufferedInputStream = inputStream.buffered()
        bufferedInputStream.mark(Integer.MAX_VALUE) // Mark the beginning of the stream

        val metadata = ObjectMetadata()
        metadata.contentType = tika.detect(bufferedInputStream)

        bufferedInputStream.reset() // Reset the stream to the beginning for the actual upload

        val objectName = generateObjectName(subDir, originalName)
        ossClient.putObject(bucketName, objectName.toString(), bufferedInputStream, metadata)
        return objectName
    }

    override fun generateUploadUrl(fileName: String, subDir: Path?, contentType: String?): String {
        val uuid = UUID.randomUUID().toString().replace("-", "")
        val extension = fileName.substringAfterLast('.', "")
        val objectKey = if (extension.isNotBlank()) "$uuid.$extension" else uuid
        val fullObjectKey = if (subDir != null) subDir.resolve(objectKey).toString() else objectKey

        val expiration =
            Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(presignedUrlExpirationMinutes))
        // Use HttpMethod.PUT for direct uploads, not POST
        val request = GeneratePresignedUrlRequest(bucketName, fullObjectKey, HttpMethod.PUT)
        request.expiration = expiration
        // Use the provided contentType, don't hardcode it
        request.contentType = contentType

        val url = ossClient.generatePresignedUrl(request)
        return url.toString()
    }

    private fun generateObjectName(subDir: Path?, originalName: String?): Path {
        val uuid = UUID.randomUUID().toString().replace("-", "")
        val extension = originalName?.substringAfterLast('.', "") ?: ""
        val fileName = if (extension.isNotBlank()) "$uuid.$extension" else uuid
        return if (subDir != null) subDir.resolve(fileName) else Path.of(fileName)
    }
}
