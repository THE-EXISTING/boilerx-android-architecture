package com.existing.boilerx.app.module.splashScreen

import android.os.Bundle
import com.existing.boilerx.app.R
import com.existing.boilerx.app.module.main.MainActivity
import com.existing.boilerx.common.base.mvvm.AppMvvmActivity
import com.existing.boilerx.ktx.delay

/**
 * Created by「 The Khaeng 」on 18 Sep 2017 :)
 */

class SplashScreenActivity : AppMvvmActivity() {

    companion object {
        private const val ONE_SECOND: Long = 1000
    }


    override
    fun setupLayoutView(): Int = R.layout.activity_splash_screen


    override
    fun setOverridePendingStartTransition() {
        super.setOverridePendingStartTransition()
        this.overridePendingTransition(
            R.anim.activity_open_enter,
            R.anim.activity_close_exit
        )
    }

    override
    fun setOverridePendingEndTransition() {
        super.setOverridePendingEndTransition()
        this.overridePendingTransition(
            R.anim.activity_open_enter,
            R.anim.activity_close_exit
        )
    }

    override
    fun onSetupView(savedInstanceState: Bundle?) {
        super.onSetupView(savedInstanceState)
        delay({ openActivityWithFinish(MainActivity::class.java) }, ONE_SECOND)
    }

}

