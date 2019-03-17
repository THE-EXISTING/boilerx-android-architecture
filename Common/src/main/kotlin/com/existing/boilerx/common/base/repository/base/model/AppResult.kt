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
    isFetched: Boolean,
    isCached: Boolean = false
) : ResultWrapper<T>(status, data, exception, payload, isFetched, isCached) {

    companion object {
        fun <T> success(
            data: T,
            payload: Any? = null,
            isCached: Boolean = false,
            isFetched: Boolean = false
        ): AppResult<T> {
            return AppResult(
                NextworkStatus.SUCCESS,
                data,
                null,
                payload,
                isFetched,
                isCached
            )
        }

        fun <T> loadingFromNetwork(data: T, payload: Any? = null, isFetched: Boolean = false): AppResult<T> {
            return AppResult(
                NextworkStatus.LOADING_FROM_NETWORK,
                data,
                null,
                payload,
                isFetched
            )
        }

        fun <T> loadingFromDatabase(payload: Any? = null): AppResult<T> {
            return AppResult(
                NextworkStatus.LOADING_FROM_DATABASE,
                null,
                null,
                payload,
                false,
                true
            )
        }

        fun <T> error(
            error: Throwable?,
            data: T? = null,
            payload: Any? = null,
            isFetched: Boolean = false
        ): AppResult<T> {
            return AppResult(
                NextworkStatus.ERROR,
                data,
                error,
                payload,
                isFetched
            )
        }

        fun <T> consume(resource: AppResult<T>): AppResult<T> {
            resource.status = NextworkStatus.CONSUME
            return resource
        }

    }
}