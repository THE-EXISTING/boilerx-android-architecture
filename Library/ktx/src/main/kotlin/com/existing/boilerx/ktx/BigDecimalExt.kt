@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("MathKt")

package com.existing.boilerx.ktx

import java.math.BigDecimal
import java.math.BigInteger

/**
 * Enables the use of the `+` operator for [BigInteger] instances.
 */
operator fun BigDecimal.plus(other: Int): BigDecimal = this.add(other.toBigDecimal())
operator fun BigDecimal.plus(other: Float): BigDecimal = this.add(other.toBigDecimal())
operator fun BigDecimal.plus(other: Double): BigDecimal = this.add(other.toBigDecimal())

/**
 * Enables the use of the `-` operator for [BigInteger] instances.
 */
operator fun BigDecimal.minus(other: Int): BigDecimal = this.subtract(other.toBigDecimal())
operator fun BigDecimal.minus(other: Float): BigDecimal = this.subtract(other.toBigDecimal())
operator fun BigDecimal.minus(other: Double): BigDecimal = this.subtract(other.toBigDecimal())

/**
 * Enables the use of the `*` operator for [BigInteger] instances.
 */
operator fun BigDecimal.times(other: Int): BigDecimal = this.multiply(other.toBigDecimal())
operator fun BigDecimal.times(other: Float): BigDecimal = this.multiply(other.toBigDecimal())
operator fun BigDecimal.times(other: Double): BigDecimal = this.multiply(other.toBigDecimal())

/**
 * Enables the use of the `/` operator for [BigInteger] instances.
 */
operator fun BigDecimal.div(other: Int): BigDecimal = this.divide(other.toBigDecimal())
operator fun BigDecimal.div(other: Float): BigDecimal = this.divide(other.toBigDecimal())
operator fun BigDecimal.div(other: Double): BigDecimal = this.divide(other.toBigDecimal())


/**
 * Enables the use of the `%` operator for [BigInteger] instances.
 */
operator fun BigDecimal.rem(other: Int): BigDecimal = this.remainder(other.toBigDecimal())
operator fun BigDecimal.rem(other: Float): BigDecimal = this.remainder(other.toBigDecimal())
operator fun BigDecimal.rem(other: Double): BigDecimal = this.remainder(other.toBigDecimal())




