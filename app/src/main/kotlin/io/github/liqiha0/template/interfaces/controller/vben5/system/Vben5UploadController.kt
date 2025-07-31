package io.github.liqiha0.template.interfaces.controller.vben5.system

import io.github.liqiha0.template.storage.application.Storage
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/vben5/upload")
@Tag(name = "Vben5/上传文件")
@ConditionalOnBean(Storage::class)
class Vben5UploadController(
    val storage: Storage,
) {
    @PostMapping
    fun upload(@RequestParam("file") file: MultipartFile): Map<String, String> {
        val path = storage.save(file.inputStream, private = false)
        val url = storage.getPublicUrl(path)
        return mapOf("url" to url)
    }
}