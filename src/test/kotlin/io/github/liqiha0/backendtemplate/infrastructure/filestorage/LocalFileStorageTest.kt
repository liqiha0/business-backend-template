package io.github.liqiha0.backendtemplate.infrastructure.filestorage

import io.github.liqiha0.backendtemplate.domain.model.file.FileKind
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

class LocalFileStorageTest {
    private val storagePath: Path = Files.createTempDirectory("storage_test")
    private val fileStorageService = LocalFileStorage(storagePath)

    @Test
    fun save() {
        val inputStream: InputStream = ByteArrayInputStream("Hello world!".toByteArray())
        val fileName = fileStorageService.save(inputStream)
        assert(storagePath.resolve("public").resolve(FileKind.TEST.name).resolve(fileName).exists())
    }
}