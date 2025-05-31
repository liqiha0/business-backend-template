package io.github.liqiha0.backendtemplate.interfaces.controller.vben5.system

import io.github.liqiha0.backendtemplate.application.system.FileStorage
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
@ConditionalOnBean(FileStorage::class)
class Vben5UploadController(
    val fileStorage: FileStorage,
) {
    @PostMapping
    fun upload(@RequestParam("file") file: MultipartFile): Map<String, String> {
        val path = fileStorage.save(file.inputStream, isPrivate = false)
        val url = fileStorage.getPublicUrl(path)
        return mapOf("url" to url)
    }
}