package io.github.liqiha0.template.gis

import io.github.liqiha0.template.core.ModuleFlywayDescriptor
import org.springframework.stereotype.Component

@Component
class GisModuleFlywayDescriptor : ModuleFlywayDescriptor {
    override val location: String = "db/migration_gis"
    override val historyTableSuffix: String = "gis"
}