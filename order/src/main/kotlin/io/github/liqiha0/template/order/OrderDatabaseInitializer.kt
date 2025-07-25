package io.github.liqiha0.template.order

import io.github.liqiha0.template.core.DatabaseInitializer
import org.flywaydb.core.Flyway
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class OrderDatabaseInitializer : DatabaseInitializer {
    override fun getFlyway(dataSource: DataSource): Flyway {
        return Flyway.configure().dataSource(dataSource).table("flyway_schema_history_order")
            .locations("db/migration_order").baselineOnMigrate(true).baselineVersion("0").load()
    }
}
