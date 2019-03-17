package com.existing.nextwork.operator


import android.accounts.NetworkErrorException
import android.os.Build

import com.existing.nextwork.BuildConfig
import com.existing.nextwork.exception.NextworkException

import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.Function
import timber.log.Timber

/**
 * Created by「 The Khaeng 」on 16 Oct 2017 :)
 */

class NextworkLogError<T>(private val overrideMessage: String = "") : Function<Throwable, SingleSource<T>> {

    @Throws(Exception::class)
    override
    fun apply(throwable: Throwable): SingleSource<T> {
        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (throwable is NextworkException && throwable.urlLog.isNotEmpty()) {
                    throwable.addSuppressed(NetworkErrorException(throwable.urlLog))
                } else if (overrideMessage.isNotEmpty()) {
                    throwable.addSuppressed(NetworkErrorException(overrideMessage))
                }
            }

            if (throwable is NextworkException && throwable.urlLog.isNotEmpty()) {
                NLog.e(throwable.urlLog, throwable)
            } else if (overrideMessage.isNotEmpty()) {
                NLog.e(overrideMessage, throwable)
            } else {
                Timber.w("Url log is empty.")
            }
        }
        return Single.error(throwable)
    }
}
