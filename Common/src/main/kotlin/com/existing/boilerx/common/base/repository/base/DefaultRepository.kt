package com.existing.boilerx.common.base.repository.base

import com.existing.nextwork.engine.AppExecutors

/**
 * Created by「 The Khaeng 」on 01 Feb 2018 :)
 */
open class DefaultRepository(
        var appExecutors: AppExecutors
) {

    companion object {
        @Volatile
        private var INSTANCE: DefaultRepository? = null

        fun getInstance(appExecutors: AppExecutors = AppExecutors.getInstance()): DefaultRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: DefaultRepository(appExecutors).also { INSTANCE = it }
                }
    }





}