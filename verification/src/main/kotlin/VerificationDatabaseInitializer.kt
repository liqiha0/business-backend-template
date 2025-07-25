package io.github.liqiha0.template.verification

import io.github.liqiha0.template.core.DatabaseInitializer
import org.flywaydb.core.Flyway
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class VerificationDatabaseInitializer : DatabaseInitializer {
    override fun getFlyway(dataSource: DataSource): Flyway {
        return Flyway.configure().dataSource(dataSource).table("flyway_schema_history_verification")
            .locations("db/migration_verification").baselineOnMigrate(true).baselineVersion("0").load()
    }
}
