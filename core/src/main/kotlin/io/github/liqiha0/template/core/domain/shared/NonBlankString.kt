package io.github.liqiha0.template.core.domain.shared

@JvmInline
value class NonBlankString(val value: String) {
    init {
        require(value.isNotBlank())
    }

    override fun toString(): String = value
}

