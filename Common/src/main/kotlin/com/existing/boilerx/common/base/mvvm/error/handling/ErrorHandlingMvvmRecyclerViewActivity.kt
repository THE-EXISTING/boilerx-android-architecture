package com.existing.boilerx.common.base.mvvm.error.handling

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.existing.boilerx.common.R
import com.existing.boilerx.common.base.mvvm.animation.AnimationHelperMvvmRecyclerViewActivity
import com.existing.boilerx.common.snackbar.showSnackbarError
import com.existing.boilerx.common.snackbar.showSnackbarMessageDismiss
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import com.existing.boilerx.ktx.map


/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

abstract class ErrorHandlingMvvmRecyclerViewActivity<VH : BaseViewHolder>
    : AnimationHelperMvvmRecyclerViewActivity<VH>(),
    ErrorHandlingInterface {

    override
    var targetView: ViewGroup? = null

    override
    fun showServiceSocketTimeout() {
        showSnackbarError(getString(R.string.alert_error_service_socket_timeout), targetView)
    }

    override
    fun showServiceUnavailableSnackbar() {
        showSnackbarError(getString(R.string.alert_error_service_unavailable), targetView)
    }

    override
    fun showNoInternetConnectionSnackbar() {
        showSnackbarMessageDismiss(getString(R.string.alert_error_service_no_internet), targetView)
    }

    fun mapDefaultErrorHandling(triggerLiveData: LiveData<Throwable>): LiveData<Throwable?> {
        return triggerLiveData.map(ErrorHandlingFunction(this))
    }

}

