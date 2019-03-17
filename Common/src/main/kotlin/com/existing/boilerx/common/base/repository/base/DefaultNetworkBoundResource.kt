package com.existing.boilerx.common.base.repository.base

import com.existing.nextwork.engine.AppExecutors
import com.existing.nextwork.engine.bound.NextworkBoundResource
import com.existing.nextwork.operator.NLog
import com.existing.boilerx.common.base.repository.base.model.AppResult
import com.existing.boilerx.common.base.repository.base.model.AppResponse
import io.reactivex.Flowable

open class DefaultNetworkBoundResource<ResultType, RequestType>(
    appExecutor: AppExecutors,
    createCall: () -> Flowable<AppResponse<RequestType>>,
    onConvertToResultType: (RequestType) -> ResultType,
    onSaveCallResult: (ResultType) -> Unit = {},
    onFetchFailed: (Throwable?) -> Unit = { NLog.e(prefixLog, "Load from database: onFetchFailed()") },
    payloadBack: Any? = null,
    prefixLog: String = ""
) : NextworkBoundResource<
        ResultType,
        RequestType,
        AppResponse<RequestType>,
        AppResult<ResultType>>(
    appExecutor,
    creator(),
    createCall,
    onConvertToResultType,
    onSaveCallResult,
    onFetchFailed = onFetchFailed,
    payloadBack = payloadBack,
    prefixLog = prefixLog
)


