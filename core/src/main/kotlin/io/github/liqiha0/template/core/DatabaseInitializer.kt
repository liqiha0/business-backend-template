package io.github.liqiha0.template.core

import org.flywaydb.core.Flyway
import javax.sql.DataSource

interface DatabaseInitializer {
    fun getFlyway(dataSource: DataSource): Flyway
}