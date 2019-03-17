package com.existing.boilerx.ktx

import android.os.Build

/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

val isBeforeOreo: Boolean get() = isOlderVersionThen(26)
val isOreoOrNewer: Boolean get() = isOnVersionOrNewer(26)
val isBeforeNougat: Boolean get() = isOlderVersionThen(24)
val isNougatOrNewer: Boolean get() = isOnVersionOrNewer(24)
val isBeforeMarshmallow: Boolean get() = isOlderVersionThen(23)
val isMarshmallowOrNewer: Boolean get() = isOnVersionOrNewer(23)
val isBeforeLollipop: Boolean get() = isOlderVersionThen(21)
val isLollipopOrNewer: Boolean get() = isOnVersionOrNewer(21)
val isBeforeKitkat: Boolean get() = isOlderVersionThen(19)
val isKitkatOrNewer: Boolean get() = isOnVersionOrNewer(19)
val isBeforeIcs: Boolean get() = isOlderVersionThen(14)
val isIcsOrNewer: Boolean get() = isOnVersionOrNewer(14)
val isCurrentVersion: Int get() = Build.VERSION.SDK_INT

fun isBeforeVersion(version: Int): Boolean = isOlderVersionThen(version)
fun isVersionOrNewer(version: Int): Boolean = isOnVersionOrNewer(version)

private fun isOlderVersionThen(version: Int) = Build.VERSION.SDK_INT < version

private fun isOnVersionOrNewer(version: Int) = Build.VERSION.SDK_INT >= version