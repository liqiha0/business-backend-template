package io.github.liqiha0.template.core

import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.stereotype.Component
import javax.sql.DataSource
import kotlin.reflect.KClass

private val logger = LoggerFactory.getLogger(DependencyBasedFlywayMigrationStrategy::class.java)

@Component
class DependencyBasedFlywayMigrationStrategy(
    private val dataSource: DataSource,
    private val descriptors: List<ModuleFlywayDescriptor>
) : FlywayMigrationStrategy {


    override fun migrate(flyway: Flyway) {
        val sortedDescriptors = topologicalSort(descriptors)

        sortedDescriptors.forEach { descriptor ->
            val moduleName = descriptor::class.simpleName
            try {
                logger.info("为模块 {} 执行 Flyway 数据库迁移", moduleName)
                Flyway.configure()
                    .dataSource(dataSource)
                    .locations(descriptor.location)
                    .table("flyway_schema_history_${descriptor.historyTableSuffix}")
                    .baselineOnMigrate(true)
                    .baselineVersion("0")
                    .load()
                    .migrate()
            } catch (e: Exception) {
                logger.error("模块 {} 的数据库迁移失败", moduleName, e)
                throw IllegalStateException("模块 '$moduleName' 的数据库迁移执行失败。", e)
            }
        }

        flyway.migrate()
    }

    private fun topologicalSort(descriptors: List<ModuleFlywayDescriptor>): List<ModuleFlywayDescriptor> {
        val descriptorMap = descriptors.associateBy { it::class }
        val inDegree = descriptors.associate { it::class to 0 }.toMutableMap()
        val graph =
            descriptors.associate { it::class to mutableListOf<KClass<out ModuleFlywayDescriptor>>() }.toMutableMap()

        for (descriptor in descriptors) {
            for (dependency in descriptor.dependencies) {
                graph[dependency]?.add(descriptor::class)
                inDegree[descriptor::class] = inDegree.getOrDefault(descriptor::class, 0) + 1
            }
        }

        val queue = descriptors.filter { inDegree[it::class] == 0 }.map { it::class }.toMutableList()
        val sortedList = mutableListOf<ModuleFlywayDescriptor>()

        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            descriptorMap[current]?.let { sortedList.add(it) }

            graph[current]?.forEach { neighbor ->
                inDegree[neighbor] = inDegree.getOrDefault(neighbor, 0) - 1
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor)
                }
            }
        }

        if (sortedList.size != descriptors.size) {
            throw IllegalStateException("在模块化数据库迁移描述符 (ModuleFlywayDescriptor) 中检测到循环依赖，请检查模块间的依赖关系。")
        }

        return sortedList
    }
}
