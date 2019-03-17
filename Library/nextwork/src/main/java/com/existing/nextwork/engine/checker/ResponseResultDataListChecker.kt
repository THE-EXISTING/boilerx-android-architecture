package com.existing.nextwork.engine.checker

import android.os.Build
import com.existing.nextwork.engine.model.NextworkResponseResult
import com.existing.nextwork.exception.EmptyDataListException
import io.reactivex.Single
import retrofit2.Response

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

open class ResponseResultDataListChecker<T : NextworkResponseResult<*>> : ResponseResultDataChecker<T>() {

    override
    fun apply(upstream: Single<Response<T>>): Single<Response<T>> {
        return super.apply(upstream)
                .flatMap {
                    val data: Any? = it.body()?.data
                    if (data is Collection<*> && data.isEmpty()) {
                        Single.error(EmptyDataListException(it.body()?.error, urlLog).apply {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                addSuppressed(Exception(data.javaClass.simpleName + ": data list is empty."))
                            }
                        })
                    } else {
                        Single.just(it)
                    }
                }
    }

}
