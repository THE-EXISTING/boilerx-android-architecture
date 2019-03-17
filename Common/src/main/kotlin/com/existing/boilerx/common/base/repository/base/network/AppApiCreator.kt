package com.existing.boilerx.common.base.repository.base.network


import com.existing.nextwork.engine.NextworkApiCreator
import okhttp3.OkHttpClient




/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

abstract class AppApiCreator<T>(apiClass: Class<T>) : NextworkApiCreator<T>(apiClass) {

    companion object {
        const val BASE_URL = "https://nuuneoi.com/courses/500px/"
    }

    override
    fun getBaseUrl(): String = BASE_URL

    override
    fun getClient(): OkHttpClient = DefaultClient.okHttpClient

}
