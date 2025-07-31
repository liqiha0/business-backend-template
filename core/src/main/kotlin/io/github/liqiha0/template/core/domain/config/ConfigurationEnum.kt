package io.github.liqiha0.template.core.domain.config

interface ConfigurationEnum<E, T> where E : Enum<E>, E : ConfigurationEnum<E, T> {
    val defaultValue: T?
    val valueType: Class<T>
}
