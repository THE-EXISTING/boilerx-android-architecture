package com.existing.boilerx.common.base.mvvm.error.handling

import com.existing.nextwork.exception.InternalErrorException
import com.existing.nextwork.exception.NoInternetConnectionException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

/**
 * Created by「 The Khaeng 」on 30 Mar 2018 :)
 */
class ErrorHandlingFunction(errorHandling: ErrorHandlingInterface) : Function1<Throwable, Throwable?> {

    var error: WeakReference<ErrorHandlingInterface> = WeakReference(errorHandling)


    override
    fun invoke(throwable: Throwable): Throwable? {
        return when (throwable) {
            is SocketTimeoutException -> {
                error.get()?.showServiceSocketTimeout()
                null
            }
            is InternalErrorException -> {
                error.get()?.showServiceUnavailableSnackbar()
                null
            }
            is NoInternetConnectionException -> {
                error.get()?.showNoInternetConnectionSnackbar()
                null
            }
            else -> throwable
        }
    }
}