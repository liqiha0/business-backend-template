package io.github.liqiha0.template.order

import io.github.liqiha0.template.core.CoreModuleFlywayDescriptor
import io.github.liqiha0.template.core.ModuleFlywayDescriptor
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class OrderModuleFlywayDescriptor : ModuleFlywayDescriptor {
    override val location: String = "db/migration_order"
    override val historyTable: String = "flyway_schema_history_order"
    override val dependencies: Set<KClass<out ModuleFlywayDescriptor>>
        get() = setOf(CoreModuleFlywayDescriptor::class)
}
