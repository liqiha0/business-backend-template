package io.github.liqiha0.template.storage.infrastructure.filestorage

import io.github.liqiha0.template.storage.application.Storage
import org.apache.tika.Tika
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*

internal const val PUBLIC_ENDPOINT = "/file"

class LocalStorage(
    val storagePath: Path,
    val publicBaseUrl: String,
) : Storage {

    override fun getPublicUrl(path: Path): String {
        return this.publicBaseUrl + PUBLIC_ENDPOINT + "?path=" + path.toString()
    }

    override fun read(path: Path): InputStream {
        val filePath = storagePath.resolve(path)

        if (!Files.exists(filePath)) {
            throw FileNotFoundException("File path: $path")
        }

        return Files.newInputStream(filePath)
    }

    override fun save(inputStream: InputStream, subDir: Path?, originalName: String?, private: Boolean): Path {
        val fileExtension = originalName?.substringAfterLast('.', "") ?: ""
        val fileName = if (fileExtension.isNotBlank()) {
            "${UUID.randomUUID()}.$fileExtension"
        } else {
            UUID.randomUUID().toString()
        }

        val filePath = if (subDir != null) {
            subDir.resolve(fileName)
        } else {
            Path.of(fileName)
        }

        val targetPath = storagePath.resolve(filePath)
        Files.createDirectories(targetPath)
        inputStream.use { input ->
            Files.copy(input, targetPath, StandardCopyOption.REPLACE_EXISTING)
        }
        return filePath
    }
}

@Controller
@ConditionalOnProperty(prefix = "storage", name = ["backend"], havingValue = "LOCAL")
class PublicFileEndpoint(
    private val storage: Storage,
) {
    private val tika = Tika()

    @GetMapping(PUBLIC_ENDPOINT)
    fun read(@RequestParam path: Path): ResponseEntity<InputStreamResource> {
        try {
            val inputStream = this.storage.read(path)
            val bufferedInputStream = if (inputStream.markSupported()) inputStream else BufferedInputStream(inputStream)
            val mimeType = tika.detect(bufferedInputStream)
            val contentType = MediaType.parseMediaType(mimeType)
            val headers = HttpHeaders()
            headers.contentType = contentType
            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes")
            return ResponseEntity.ok()
                .headers(headers)
                .body(InputStreamResource(bufferedInputStream))
        } catch (e: FileNotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }
}
