package com.existing.nextwork.operator

import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

class BackgroundThreadTransformer<T> : SingleTransformer<T, T> {
    override
    fun apply(upstream: Single<T>): Single<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
