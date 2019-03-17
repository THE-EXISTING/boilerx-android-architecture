package com.existing.nextwork.engine.checker

import com.existing.nextwork.engine.model.NextworkResponseResult
import io.reactivex.Single
import retrofit2.Response

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

open class ResponseResultManualChecker<T : NextworkResponseResult<*>>(val callback: (response: T?) -> Exception?) : ResponseResultChecker<T>() {

    override
    fun apply(upstream: Single<Response<T>>): Single<Response<T>> {
        return super.apply(upstream)
                .flatMap {
                    val exception = callback.invoke(it.body())
                    if (exception != null) {
                        Single.error(exception)
                    } else {
                        Single.just(it)
                    }
                }
    }

}
