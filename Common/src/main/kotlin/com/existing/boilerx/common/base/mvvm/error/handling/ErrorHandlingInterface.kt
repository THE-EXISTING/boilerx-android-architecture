package com.existing.boilerx.common.base.mvvm.error.handling

import android.view.ViewGroup

/**
 * Created by「 The Khaeng 」on 30 Mar 2018 :)
 */
interface ErrorHandlingInterface {

    var targetView: ViewGroup?

    fun showServiceSocketTimeout()

    fun showServiceUnavailableSnackbar()

    fun showNoInternetConnectionSnackbar()
}