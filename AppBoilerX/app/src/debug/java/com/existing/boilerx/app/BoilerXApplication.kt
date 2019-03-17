package com.existing.boilerx.app

import com.facebook.stetho.Stetho
import timber.log.Timber

/**
* Created by「 The Khaeng 」on 28 Aug 2017 :)
*/

class BoilerXApplication : MainApplication() {

    override
    fun onCreate() {
        super.onCreate()
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                                  .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                                  .build())
        Timber.plant(DebugTree())
        androidx.fragment.app.FragmentManager.enableDebugLogging(true)
    }

}
