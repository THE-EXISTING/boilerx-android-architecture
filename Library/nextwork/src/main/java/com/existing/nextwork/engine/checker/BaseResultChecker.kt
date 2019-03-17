package com.existing.nextwork.engine.checker

import com.existing.nextwork.exception.*
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.functions.Function
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Created by「 The Khaeng 」on 26 Feb 2018 :)
 */

abstract class BaseResultChecker<T> : SingleTransformer<T, T> {

    var urlLog: String = ""


    fun checkNullBodyException(body: Any?, urlLog: String): Exception? {
        if (body == null) {
            return NullBodyException(
                "Response body() null result please check it.",
                urlLog
            )
        }
        return null
    }

    fun checkResultCodeException(code: Int, message: String, urlLog: String): Exception? {
        return when {
            code == 404 -> ServerNotFoundException(404, urlLog)
            code == 500 -> InternalErrorException(500, urlLog)
            code >= 400 -> StatusCodeException(code, message, urlLog)
            else -> null
        }
    }

    fun checkConnectExceptionSingle(): Function<Throwable, SingleSource<T>> {
        return Function { throwable ->
            if (throwable is UnknownHostException || throwable is ConnectException) {
                return@Function Single.error<T>(NoInternetConnectionException(throwable.message, urlLog))
            }
            return@Function Single.error<T>(throwable)
        }
    }

}
