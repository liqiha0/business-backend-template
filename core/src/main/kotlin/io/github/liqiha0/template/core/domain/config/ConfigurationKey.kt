package io.github.liqiha0.template.core.domain.config

import java.math.BigDecimal

interface ConfigurationKey<T> {
    val keyGroup: String

    val keyName: String

    val valueType: Class<T>

    val defaultValue: T?
}

data class StringKey(
    override val keyGroup: String,
    override val keyName: String,
    override val defaultValue: String? = null
) : ConfigurationKey<String> {
    override val valueType: Class<String> get() = String::class.java
}

data class IntKey(
    override val keyGroup: String,
    override val keyName: String,
    override val defaultValue: Int? = null
) : ConfigurationKey<Int> {
    override val valueType: Class<Int> get() = Int::class.java
}

data class BooleanKey(
    override val keyGroup: String,
    override val keyName: String,
    override val defaultValue: Boolean? = null
) : ConfigurationKey<Boolean> {
    override val valueType: Class<Boolean> get() = Boolean::class.java
}

data class BigDecimalKey(
    override val keyGroup: String,
    override val keyName: String,
    override val defaultValue: BigDecimal? = null
) : ConfigurationKey<BigDecimal> {
    override val valueType: Class<BigDecimal> get() = BigDecimal::class.java
}
