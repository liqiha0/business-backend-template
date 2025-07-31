package io.github.liqiha0.template.core.domain.config

/**
 * Represents a strongly-typed key for a system configuration value.
 *
 * @param T The type of the value this key refers to.
 */
interface ConfigurationKey<T> {
    /**
     * The group of the key, typically the enum class name.
     */
    val keyGroup: String

    /**
     * The name of the key, typically the enum constant name.
     */
    val keyName: String

    /**
     * The expected data type of the configuration value.
     */
    val valueType: Class<T>

    /**
     * The default value for this configuration.
     */
    val defaultValue: T?
}

/**
 * A concrete implementation of [ConfigurationKey] for [String] values.
 */
data class StringKey(
    override val keyGroup: String,
    override val keyName: String,
    override val defaultValue: String? = null
) : ConfigurationKey<String> {
    override val valueType: Class<String> get() = String::class.java
}

/**
 * A concrete implementation of [ConfigurationKey] for [Int] values.
 */
data class IntKey(
    override val keyGroup: String,
    override val keyName: String,
    override val defaultValue: Int? = null
) : ConfigurationKey<Int> {
    override val valueType: Class<Int> get() = Int::class.java
}

/**
 * A concrete implementation of [ConfigurationKey] for [Boolean] values.
 */
data class BooleanKey(
    override val keyGroup: String,
    override val keyName: String,
    override val defaultValue: Boolean? = null
) : ConfigurationKey<Boolean> {
    override val valueType: Class<Boolean> get() = Boolean::class.java
}
