package com.existing.boilerx.common.base.repository.base

import com.existing.boilerx.common.base.repository.base.model.AppResponse
import com.existing.boilerx.common.base.repository.base.model.AppResult
import com.existing.nextwork.engine.AppExecutors
import com.existing.nextwork.engine.NextworkResourceCreator
import com.existing.nextwork.engine.bound.NextworkCacheBoundResource
import com.existing.nextwork.operator.NLog
import io.reactivex.Flowable

class DefaultNetworkCacheBoundResource<ResultType, RequestType>(
        appExecutor: AppExecutors,
        loadFromDb: () -> Flowable<ResultType>,
        onShouldFetch: (List<ResultType?>) -> Boolean,
        createCall: () -> Flowable<AppResponse<RequestType>>,
        onConvertToResultType: (RequestType) -> ResultType,
        onSaveCallResult: (ResultType) -> Unit,
        onFetchFailed: (Throwable?) -> Unit = { NLog.e(prefixLog, "Load from database: onFetchFailed()") },
        payloadBack: Any? = null,
        prefixLog: String = "")
    : NextworkCacheBoundResource<
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
        prefixLog = prefixLog )


fun <ResultType> creator(): NextworkResourceCreator<ResultType, AppResult<ResultType>> =
        object : NextworkResourceCreator<ResultType, AppResult<ResultType>> {

            override
            fun loadingFromDatabase(payload: Any?): AppResult<ResultType> {
                return AppResult.loadingFromDatabase(payload)
            }

            override
            fun loadingFromNetwork(payload: Any?): AppResult<ResultType> {
                return AppResult.loadingFromNetwork(payload)
            }

            override
            fun completed(payload: Any?): AppResult<ResultType> {
                return AppResult.completed(payload)
            }

            override
            fun next(newData: ResultType, payload: Any?, isCache: Boolean): AppResult<ResultType> {
                return AppResult.next(newData, payload, isCache)
            }


            override
            fun error(error: Throwable?, payload: Any?): AppResult<ResultType> {
                return AppResult.error(error, payload)
            }
        }

