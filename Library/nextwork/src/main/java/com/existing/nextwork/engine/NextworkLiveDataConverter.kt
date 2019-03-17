package com.existing.nextwork.engine


import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import android.content.ContentValues.TAG
import android.util.Log
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by「 The Khaeng 」on 12 Oct 2017 :)
 */

fun <T> Single<T>.toLiveData(isBindLifecycle: Boolean = false): LiveData<T> {
    val single = this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    if (isBindLifecycle) {
        return LiveDataReactiveStreams.fromPublisher(single.toFlowable())
    } else {
        val liveData: MutableLiveData<T> = MutableLiveData()
        single
            .subscribe(object : SingleObserver<T?> {
                override
                fun onSuccess(t: T) {
                    liveData.value = t
                }


                override
                fun onSubscribe(d: Disposable) {
                }

                override
                fun onError(e: Throwable) {
                    if (e is NullPointerException) {
                        Log.e(TAG, "Null value")
                        liveData.value = null
                    }
                }

            })
        return liveData

    }
}
