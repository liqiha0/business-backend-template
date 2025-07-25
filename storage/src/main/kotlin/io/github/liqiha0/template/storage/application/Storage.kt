package io.github.liqiha0.template.storage.application

import java.io.InputStream
import java.nio.file.Path

interface Storage {
    fun getPublicUrl(path: Path): String
    fun read(path: Path): InputStream
    fun save(
        inputStream: InputStream,
        subDir: Path? = null,
        originalName: String? = null,
        private: Boolean = true,
    ): Path
}