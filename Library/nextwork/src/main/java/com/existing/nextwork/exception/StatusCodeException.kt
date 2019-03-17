package com.existing.nextwork.exception

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

open class StatusCodeException(
        val statusCode: Int = 0,
        message: String? = "",
        urlLog: String = "") : NextworkException(message?.trim { it <= ' ' }, urlLog) {
}
