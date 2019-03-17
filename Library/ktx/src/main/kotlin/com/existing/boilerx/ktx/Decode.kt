package com.existing.boilerx.ktx

/**
 * Created by「 The Khaeng 」on 28 Jun 2018 :)
 */

fun String.encodeToInt(): Int {
    var intValue = 0
    this.toCharArray().forEach {
        intValue += it.toByte()
    }
    return intValue
}

fun String.encodeToLong(): Long {
    var intValue = 0L
    this.toCharArray().forEach {
        intValue += it.toByte()
    }
    return intValue
}