package com.existing.nextwork.engine.checker

import com.existing.nextwork.engine.model.ResponseWrapper
import com.existing.nextwork.engine.model.NextworkResponseResult
import io.reactivex.Single

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

open class NextworkResponseResultChecker<T : NextworkResponseResult<*>> : BaseResultChecker<ResponseWrapper<T>>() {

    override
    fun apply(upstream: Single<ResponseWrapper<T>>): Single<ResponseWrapper<T>> {
        return upstream
            .flatMap { this.handleExceptionSingle(it) }
            .onErrorResumeNext(checkConnectExceptionSingle())
    }


    fun handleExceptionSingle(response: ResponseWrapper<T>): Single<ResponseWrapper<T>> {
        val code: Int = response.code
        val body: T? = response.body
        val message: String = response.error?.message ?: ""
        val method = response.method
        val url = response.url
        urlLog = "$method: $url"
        val resultCodeException = checkResultCodeException(code, message, urlLog)
        val nullBodyException = checkNullBodyException(body, urlLog)
        return when {
            resultCodeException != null -> Single.error(resultCodeException)
            nullBodyException != null -> Single.error(nullBodyException)
            else -> Single.just(response)
        }
    }


}
