package io.github.liqiha0.template.core

import org.springframework.stereotype.Component

@Component
class CoreModuleFlywayDescriptor : ModuleFlywayDescriptor {
    override val location: String = "db/migration_core"
    override val historyTableSuffix: String = "core"
}
