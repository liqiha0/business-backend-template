package io.github.liqiha0.template.core.domain.shared

import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
value class Money(val value: BigDecimal) : Comparable<Money> {
    init {
        require(value.scale() <= 2) { "Money scale must be <= 2" }
    }

    override fun toString(): String = value.toPlainString()

    override fun compareTo(other: Money): Int = value.compareTo(other.value)

    operator fun plus(other: Money): Money = moneyOf(value + other.value)
    operator fun minus(other: Money): Money = moneyOf(value - other.value)

    operator fun times(multiplier: Int): Money = moneyOf(value.multiply(BigDecimal.valueOf(multiplier.toLong())))
    operator fun times(multiplier: Long): Money = moneyOf(value.multiply(BigDecimal.valueOf(multiplier)))
    operator fun times(multiplier: BigDecimal): Money = moneyOf(value.multiply(multiplier))

    fun abs(): Money = moneyOf(value.abs())
    fun isZero(): Boolean = value.compareTo(BigDecimal.ZERO) == 0
    fun isPositive(): Boolean = value.signum() > 0
    fun isNegative(): Boolean = value.signum() < 0

    fun min(`val`: Money): Money {
        return (if (compareTo(`val`) <= 0) this else `val`)
    }
}

fun moneyOf(amount: BigDecimal, roundingMode: RoundingMode = RoundingMode.HALF_UP): Money =
    Money(amount.setScale(2, roundingMode))

fun moneyOf(amount: Long): Money = Money(BigDecimal.valueOf(amount).setScale(2))
fun moneyOf(amount: Int): Money = moneyOf(amount.toLong())
fun moneyOf(amount: String, roundingMode: RoundingMode = RoundingMode.HALF_UP): Money =
    moneyOf(BigDecimal(amount), roundingMode)

/** Create from minor units (e.g. cents). */
fun moneyFromMinorUnits(cents: Long): Money = Money(BigDecimal.valueOf(cents, 2).setScale(2))

val MoneyZero: Money = moneyOf(BigDecimal.ZERO)

