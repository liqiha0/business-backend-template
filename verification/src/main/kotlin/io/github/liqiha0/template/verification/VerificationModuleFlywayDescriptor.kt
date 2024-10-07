package io.github.liqiha0.template.verification

import io.github.liqiha0.template.core.ModuleFlywayDescriptor
import org.springframework.stereotype.Component

@Component
class VerificationModuleFlywayDescriptor : ModuleFlywayDescriptor {
    override val location: String = "db/migration_verification"
    override val historyTableSuffix: String = "verification"
}
