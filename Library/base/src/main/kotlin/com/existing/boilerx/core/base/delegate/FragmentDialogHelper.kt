package com.existing.boilerx.core.base.delegate

import android.os.Bundle

/**
 * Created by「 The Khaeng 」on 08 Oct 2017 :)
 */

interface FragmentDialogHelper {

    fun setResultData(data: Bundle?)

    fun setResultCode(resultCode: Int)

    fun show(target: androidx.fragment.app.Fragment, requestCode: Int)

    fun show(activity: androidx.fragment.app.FragmentActivity, requestCode: Int)

    fun dismiss()

    fun dismiss(resultCode: Int, extras: Bundle? = null)
}
