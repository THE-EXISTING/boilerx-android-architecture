package com.existing.nextwork.engine.model

import com.google.gson.annotations.SerializedName

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

abstract class NextworkResponseResult<T>(
        @SerializedName("data")
        var data: T? = null)
    : NextworkResponse() {

    fun hasData(): Boolean = data != null
}



