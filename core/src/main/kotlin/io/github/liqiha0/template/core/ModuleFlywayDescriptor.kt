package io.github.liqiha0.template.core

import kotlin.reflect.KClass

/**
 * 模块化 Flyway 迁移描述符接口
 *
 * 各模块通过实现此接口来注册自己的数据库迁移配置及其依赖关系
 */
interface ModuleFlywayDescriptor {

    /**
     * 此模块 Flyway 迁移脚本的位置。
     */
    val location: String

    /**
     * 定义此模块的 Flyway 历史表名称。
     * 提供一个默认实现，根据类名自动生成表名，例如：
     * CoreModuleFlywayDescriptor -> flyway_schema_history_core
     */
    // TODO: 改成历史表后缀属性，移除默认实现
    val historyTable: String
        get() {
            val className = this::class.simpleName ?: "default"
            val moduleName = className.removeSuffix("ModuleFlywayDescriptor")
                .replace(Regex("([a-z])([A-Z])"), "$1_$2")
                .lowercase()
            return "flyway_schema_history_$moduleName"
        }

    /**
     * 定义此模块所依赖的其他模块的描述符类。
     *
     * @return 一个包含依赖描述符 KClass 的集合。
     */
    val dependencies: Set<KClass<out ModuleFlywayDescriptor>>
        get() = emptySet()
}