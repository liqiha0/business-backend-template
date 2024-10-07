package io.github.liqiha0.template.core

import org.springframework.stereotype.Component

@Component
class CoreModuleFlywayDescriptor : ModuleFlywayDescriptor {
    override val location: String = "db/migration_core"
    override val historyTable: String = "flyway_schema_history_core"
}
