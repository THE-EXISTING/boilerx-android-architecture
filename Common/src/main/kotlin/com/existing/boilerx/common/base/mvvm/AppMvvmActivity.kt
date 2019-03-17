package com.existing.boilerx.common.base.mvvm

import android.os.Build
import com.existing.boilerx.common.base.mvvm.error.handling.ErrorHandlingMvvmActivity


/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

@Suppress("SortModifiers")
abstract class AppMvvmActivity
    : ErrorHandlingMvvmActivity() {


    override
    fun setOverridePendingStartTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !isInMultiWindowMode) {
            setPendingStartTransition()
        }
    }

    override
    fun setOverridePendingEndTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !isInMultiWindowMode) {
            setPendingEndTransition()
        }
    }

    open fun setPendingStartTransition() {
        super.setOverridePendingStartTransition()
    }

    open fun setPendingEndTransition() {
        super.setOverridePendingEndTransition()
    }



}

