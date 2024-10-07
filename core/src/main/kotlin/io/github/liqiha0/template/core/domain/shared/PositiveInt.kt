package io.github.liqiha0.template.core.domain.shared

@JvmInline
value class PositiveInt(val value: Int) {
    init { require(value > 0) { "must be > 0" } }
    override fun toString(): String = value.toString()
}

