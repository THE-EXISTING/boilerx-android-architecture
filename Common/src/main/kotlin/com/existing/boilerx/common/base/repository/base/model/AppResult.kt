package com.existing.boilerx.common.base.repository.base.model

import com.existing.nextwork.engine.model.NextworkStatus
import com.existing.nextwork.engine.model.ResultWrapper
import com.existing.nextwork.engine.model.Status


/**
 * Created by「 The Khaeng 」on 12 Oct 2017 :)
 */
class AppResult<T> private constructor(
        @Status status: Int,
        data: T?,
        exception: Throwable?,
        payload: Any?,
        isCached: Boolean = false)
    : ResultWrapper<T>(status, data, exception, payload, isCached) {

    fun <R> createNewData(data: R): AppResult<R> {
        return AppResult(this.status,
                         data,
                         this.exception,
                         this.payload,
                         this.isCached)
    }

    companion object {
        fun <T> next(data: T?, payload: Any? = null, isCached: Boolean = false): AppResult<T> {
            return AppResult(NextworkStatus.NEXT,
                             data,
                             null,
                             payload,
                             isCached)
        }

        fun <T> completed(payload: Any? = null): AppResult<T> {
            return AppResult(NextworkStatus.COMPLETED,
                             null,
                             null,
                             payload,
                             isCached = false)
        }

        fun <T> loadingFromNetwork(payload: Any? = null): AppResult<T> {
            return AppResult(NextworkStatus.LOADING_FROM_NETWORK,
                             null,
                             null,
                             payload )
        }

        fun <T> loadingFromDatabase(payload: Any? = null): AppResult<T> {
            return AppResult(NextworkStatus.LOADING_FROM_DATABASE,
                             null,
                             null,
                             payload,
                             isCached = true)
        }

        fun <T> error(error: Throwable?,
                      payload: Any? = null ): AppResult<T> {
            return AppResult(NextworkStatus.ERROR,
                             null,
                             error,
                             payload )
        }

        fun <T> consume(resource: AppResult<T>): AppResult<T> {
            resource.status = NextworkStatus.CONSUME
            return resource
        }

    }
}