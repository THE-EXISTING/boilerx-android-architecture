package com.existing.nextwork.cookie

import java.util.*

/**
 * Created by「 The Khaeng 」on 08 Apr 2018 :)
 */
class NextworkCookie(
        var format: String = NO_FORMAT,
        var key: String = "",
        var value: String = "",
        val options: Map<String, String> = HashMap()) {

    companion object {
        const val NO_FORMAT = ""
        const val COOKIE_FORMAT = "Set-Cookie"
        const val COOKIE_V2_FORMAT = "Set-Cookie2"
    }


    override
    fun toString(): String {
        val cookieString: StringBuilder =
                if (format.isEmpty()) {
                    StringBuilder("$key=$value")
                } else {
                    StringBuilder("$format: $key=$value")
                }

        for ((key1, value1) in options) {
            cookieString.append("; ").append(key1).append("=").append(value1)
        }
        return cookieString.toString()
    }

}
