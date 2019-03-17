@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("MathKt")

package com.existing.boilerx.ktx

import java.math.BigDecimal
import java.math.BigInteger

/**
 * Enables the use of the `+` operator for [BigInteger] instances.
 */
operator fun BigInteger.plus(other: Int): BigInteger = this.add(other.toBigInteger())
operator fun BigInteger.plus(other: Float): BigDecimal = this.toBigDecimal().add(other.toBigDecimal())
operator fun BigInteger.plus(other: Double): BigDecimal = this.toBigDecimal().add(other.toBigDecimal())

/**
 * Enables the use of the `-` operator for [BigInteger] instances.
 */
operator fun BigInteger.minus(other: Int): BigInteger = this.subtract(other.toBigInteger())
operator fun BigInteger.minus(other: Float): BigDecimal = this.toBigDecimal().subtract(other.toBigDecimal())
operator fun BigInteger.minus(other: Double): BigDecimal = this.toBigDecimal().subtract(other.toBigDecimal())

/**
 * Enables the use of the `*` operator for [BigInteger] instances.
 */
operator fun BigInteger.times(other: Int): BigInteger = this.multiply(other.toBigInteger())
operator fun BigInteger.times(other: Float): BigDecimal = this.toBigDecimal().multiply(other.toBigDecimal())
operator fun BigInteger.times(other: Double): BigDecimal = this.toBigDecimal().multiply(other.toBigDecimal())

/**
 * Enables the use of the `/` operator for [BigInteger] instances.
 */
operator fun BigInteger.div(other: Int): BigInteger = this.divide(other.toBigInteger())
operator fun BigInteger.div(other: Float): BigDecimal = this.toBigDecimal().divide(other.toBigDecimal())
operator fun BigInteger.div(other: Double): BigDecimal = this.toBigDecimal().divide(other.toBigDecimal())


/**
 * Enables the use of the `%` operator for [BigInteger] instances.
 */
operator fun BigInteger.rem(other: Int): BigInteger = this.remainder(other.toBigInteger())
operator fun BigInteger.rem(other: Float): BigDecimal = this.toBigDecimal().remainder(other.toBigDecimal())
operator fun BigInteger.rem(other: Double): BigDecimal = this.toBigDecimal().remainder(other.toBigDecimal())




