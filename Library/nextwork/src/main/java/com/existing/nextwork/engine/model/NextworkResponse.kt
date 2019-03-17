package com.existing.nextwork.engine.model

import com.google.gson.annotations.SerializedName

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

abstract class NextworkResponse(
    @SerializedName(value = "statusCode", alternate = ["status_code"])
    var statusCode: Int? = -1,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("error")
    var error: String? = ""
) {

    fun isSuccess(): Boolean = 200 == statusCode
}



