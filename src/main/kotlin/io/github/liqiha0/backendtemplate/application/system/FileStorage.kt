package io.github.liqiha0.backendtemplate.application.system

import java.io.InputStream
import java.nio.file.Path

interface FileStorage {
    fun getPublicUrl(path: Path): String
    fun read(path: Path): InputStream
    fun save(
        inputStream: InputStream,
        subDir: Path? = null,
        originalName: String? = null,
        isPrivate: Boolean = true,
    ): Path
}
