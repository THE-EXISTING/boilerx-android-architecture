package com.existing.boilerx.ktx

import android.os.SystemClock
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by「 The Khaeng 」on 08 Apr 2018 :)
 */
const val DEFAULT_DELAY = 1000L
const val DEFAULT_BOUNCE = 100L

val mapTimestamp: MutableMap<String, Long> = mutableMapOf()
fun bouncedAction(key: String, minimumIntervalMillis: Long = DEFAULT_BOUNCE, action: () -> Unit) {
    val previousClickTimestamp = mapTimestamp[key]
    val currentTimestamp = SystemClock.uptimeMillis()
    mapTimestamp[key] = currentTimestamp
    if (previousClickTimestamp == null || currentTimestamp - previousClickTimestamp > minimumIntervalMillis) {
        action()
    }
}


fun delay(action: () -> Unit, delay: Long = DEFAULT_DELAY) {
    Observable.empty<Any>()
            .delay(delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { action() }
            .subscribe()
}

fun doOnBackground(action: () -> Unit) {
    Observable.empty<Any>()
            .subscribeOn(Schedulers.computation())
            .doOnComplete { action() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
}


fun <T> doOnBackground(action: Function<Any, T>, doFinal: (T) -> Unit) {
    Observable.just(Any())
            .subscribeOn(Schedulers.computation())
            .map(action)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<T> {
                override
                fun onComplete() {
                }

                override
                fun onSubscribe(d: Disposable) {
                }

                override
                fun onNext(t: T) {
                    doFinal.invoke(t)
                }

                override
                fun onError(e: Throwable) {
                }

            })
}

