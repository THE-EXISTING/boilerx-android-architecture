package com.existing.nextwork.engine.checker

import com.existing.nextwork.engine.model.NextworkResponse
import io.reactivex.Single
import retrofit2.Response

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

open class ResponseResultChecker<T : NextworkResponse> : BaseResultChecker<Response<T>>() {


    override
    fun apply(upstream: Single<Response<T>>): Single<Response<T>> {
        return upstream
            .flatMap { this.handleExceptionSingle(it) }
            .onErrorResumeNext(checkConnectExceptionSingle())
    }


    fun handleExceptionSingle(response: Response<T>): Single<Response<T>> {
        val code: Int = response.code()
        val body: T? = response.body()
        val message: String = response.message()
        val method = response.raw().request().method()
        val url = response.raw().request().url().toString()
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
