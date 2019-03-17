package com.existing.nextwork.exception

import android.accounts.NetworkErrorException

/**
 * Created by「 The Khaeng 」on 07 Jan 2019 :)
 */
open class NextworkException(
        message: String? = "",
        val urlLog: String = ""
                       ) : NetworkErrorException(message) {

}