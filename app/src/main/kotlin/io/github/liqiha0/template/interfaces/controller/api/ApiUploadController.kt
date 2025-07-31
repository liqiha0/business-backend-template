package io.github.liqiha0.template.interfaces.controller.api

import io.github.liqiha0.template.storage.application.Storage
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/upload")
@Tag(name = "用户端/上传文件")
class ApiUploadController(
    val storage: Storage,
) {
    @PostMapping
    fun upload(@RequestParam("file") file: MultipartFile): Map<String, String> {
        val path = storage.save(file.inputStream, private = false)
        val url = storage.getPublicUrl(path)
        return mapOf("url" to url)
    }
}