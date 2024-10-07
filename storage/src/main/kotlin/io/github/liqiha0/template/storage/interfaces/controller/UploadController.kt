package io.github.liqiha0.template.storage.interfaces.controller

import io.github.liqiha0.template.storage.application.Storage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Paths
import java.util.*

@RestController
@RequestMapping("/storage")
@ConditionalOnProperty(name = ["storage.enable-upload-url-generation-api"], havingValue = "true", matchIfMissing = true)
class UploadController(private val storage: Storage) {

    @GetMapping("/generate-upload-url")
    fun generateUploadUrl(
        @RequestParam(required = false) subDir: String?,
        @RequestParam(required = false) contentType: String?
    ): GenerateUploadUrlResponse {
        val url = storage.generateUploadUrl(
            fileName = UUID.randomUUID().toString().replace("-", ""),
            subDir = subDir?.let { Paths.get(it) },
            contentType = contentType
        )
        return GenerateUploadUrlResponse(url)
    }

    data class GenerateUploadUrlResponse(
        val url: String
    )
}
