package com.existing.boilerx.common.base.repository.base

import com.existing.nextwork.engine.AppExecutors
import com.existing.nextwork.engine.NextworkResourceCreator
import com.existing.nextwork.engine.bound.NextworkCacheBoundResource
import com.existing.nextwork.operator.NLog
import com.existing.boilerx.common.base.repository.base.model.AppResponse
import com.existing.boilerx.common.base.repository.base.model.AppResult
import io.reactivex.Flowable

class DefaultNetworkCacheBoundResource<ResultType, RequestType>(
    appExecutor: AppExecutors,
    loadFromDb: () -> Flowable<ResultType>,
    onShouldFetch: (ResultType?) -> Boolean,
    createCall: () -> Flowable<AppResponse<RequestType>>,
    onConvertToResultType: (ResultType?, RequestType) -> ResultType,
    onSaveCallResult: (ResultType) -> Unit,
    onFetchFailed: (Throwable?) -> Unit = { NLog.e(prefixLog, "Load from database: onFetchFailed()") },
    payloadBack: Any? = null,
    isLoadCacheBeforeFetch: Boolean = false,
    prefixLog: String = ""
) : NextworkCacheBoundResource<
        ResultType,
        RequestType,
        AppResponse<RequestType>,
        AppResult<ResultType>>(
    appExecutor,
    creator(),
    loadFromDb,
    onShouldFetch,
    createCall,
    onConvertToResultType,
    onSaveCallResult,
    onFetchFailed = onFetchFailed,
    payloadBack = payloadBack,
    isLoadCacheBeforeFetch = isLoadCacheBeforeFetch,
    prefixLog = prefixLog
)


fun <ResultType> creator(): NextworkResourceCreator<ResultType, AppResult<ResultType>> =
    object : NextworkResourceCreator<ResultType, AppResult<ResultType>> {
        override
        fun loadingFromDatabase(payload: Any?): AppResult<ResultType> {
            return AppResult.loadingFromDatabase(payload)
        }

        override
        fun loadingFromNetwork(data: ResultType, payload: Any?, isFetch: Boolean): AppResult<ResultType> {
            return AppResult.loadingFromNetwork(data, payload, isFetch)
        }

        override
        fun success(newData: ResultType, payload: Any?, isCache: Boolean, isFetch: Boolean): AppResult<ResultType> {
            return AppResult.success(newData, payload, isCache, isFetch)
        }

        override
        fun error(error: Throwable?, oldData: ResultType, payload: Any?, isFetch: Boolean): AppResult<ResultType> {
            return AppResult.error(error, oldData, payload, isFetch)
        }
    }











