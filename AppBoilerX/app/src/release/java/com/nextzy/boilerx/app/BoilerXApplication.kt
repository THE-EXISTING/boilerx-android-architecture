package com.existing.boilerx.app

import timber.log.Timber

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

class BoilerXApplication : MainApplication() {

    override
    fun onCreate() {
        super.onCreate()
        Timber.plant(ReleaseTree())
    }

    override
    fun onTerminate() {
        super.onTerminate()
    }

}
