package com.existing.boilerx.app.module.crash

import android.content.Intent
import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.existing.boilerx.app.MainApplication
import com.existing.boilerx.app.R
import com.existing.boilerx.app.module.main.MainActivity
import com.existing.boilerx.common.base.mvvm.AppMvvmActivity
import timber.log.Timber
import kotlinx.android.synthetic.main.activity_crash.btn_restart as btnRestart


/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

class CrashActivity : AppMvvmActivity() {

    override
    fun setupLayoutView(): Int = R.layout.activity_crash


    override
    fun onPrepareInstanceState() {
        super.onPrepareInstanceState()
        repeatShowErrorStackTrace()
    }

    override
    fun onSetupView(savedInstanceState: Bundle?) {
        super.onSetupView(savedInstanceState)
        btnRestart.setOnClickListener { restartApplication() }

    }


    private fun restartApplication() {
        val intent = Intent(this, MainActivity::class.java)
        CustomActivityOnCrash.restartApplicationWithIntent(
                this,
                intent,
                MainApplication.crashConfig)
    }

    /* =========================== Private method =============================================== */
    private fun repeatShowErrorStackTrace() {
        val stackTrace: String? = CustomActivityOnCrash.getStackTraceFromIntent(intent)
        stackTrace?.let { Timber.e(stackTrace) }
    }

}

