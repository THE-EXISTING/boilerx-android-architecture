package com.existing.nextwork.engine.checker

import android.os.Build
import com.existing.nextwork.engine.model.ResponseWrapper
import com.existing.nextwork.engine.model.NextworkResponseResult
import com.existing.nextwork.exception.NullDataException
import io.reactivex.Single

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

open class NextworkResponseResultDataChecker<T : NextworkResponseResult<*>> : NextworkResponseResultChecker<T>() {


    override
    fun apply(upstream: Single<ResponseWrapper<T>>): Single<ResponseWrapper<T>> {
        return super.apply(upstream)
            .flatMap {
                when {
                    it.body?.data == null -> Single.error(NullDataException(it.body?.error, urlLog).apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            addSuppressed(Exception(it.body?.javaClass?.simpleName + ": data is null."))
                        }
                    })
                    else -> Single.just(it)
                }
            }
    }

}
