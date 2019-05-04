package com.existing.nextwork.engine.bound.livedata


import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.existing.nextwork.engine.AppExecutors
import com.existing.nextwork.engine.NextworkResourceCreator
import com.existing.nextwork.engine.model.ResponseWrapper
import com.existing.nextwork.engine.model.ResultWrapper
import com.existing.nextwork.operator.NLog
import retrofit2.Response


/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 *
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
open class NextworkCacheBoundResourceLiveData<
        ResultType,
        RequestType,
        ResponseApiType : ResponseWrapper<RequestType>,
        ResourceType : ResultWrapper<ResultType>>
@MainThread
constructor(
        private val appExecutors: AppExecutors,
        private val creator: NextworkResourceCreator<ResultType, ResourceType>,
        @MainThread
        private val loadFromDb: () -> LiveData<ResultType>,
        @MainThread
        private val onShouldFetch: (ResultType?) -> Boolean,
        @MainThread
        private val createCall: () -> LiveData<ResponseApiType>,
        private val onConvertToResultType: (RequestType) -> ResultType,
        private val onSaveCallResult: (ResultType) -> Unit,
        @WorkerThread
        private val onProcessResponse: (ResponseApiType?) -> RequestType? = { response ->
            val body = response?.body
            if (body is Response<*>) {
                body.body() as RequestType?
            } else {
                body
            }
        },
        private val onFetchFailed: (Throwable?) -> Unit = { NLog.e(prefixLog, "Load from database: onFetchFailed()") },
        private val isLoadCacheBeforeFetch: Boolean = false,
        private val payloadBack: Any? = null,
        private val prefixLog: String = "",
        private val result: MediatorLiveData<ResourceType> = MediatorLiveData()
           ) {


    init {
        result.value = creator.loadingFromDatabase(payloadBack)
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            NLog.i(prefixLog, "Load from database (old): $data")
            result.removeSource(dbSource)
            val shouldFetch = onShouldFetch(data)
            NLog.i(prefixLog, "ShouldFetch: $shouldFetch")
            if (shouldFetch) {
                if (isLoadCacheBeforeFetch)
                    result.value = creator.next(data, payloadBack, true)
                fetchFromNetwork(dbSource, data)
            } else {
                result.addSource(dbSource) { oldData ->
                    result.setValue(
                            creator.next(
                                    oldData,
                                    payloadBack,
                                    true
                                        )
                                   )
                }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>, oldData: ResultType?) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { data -> result.setValue(creator.loadingFromNetwork(payloadBack)) }
        result.addSource(apiResponse) { response ->
            NLog.i(prefixLog, "CreateCall next: [${response?.method}] ${response?.url}")
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response?.isSuccessful() == true) {
                appExecutors.diskIO.execute {
                    val responseResult = onProcessResponse(response)
                    if (responseResult != null) {
                        val data = onConvertToResultType(responseResult)
                        onSaveCallResult(data)
                        NLog.i(prefixLog, "Save call result: $data")
                        appExecutors.mainThread.execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            val dataSource = loadFromDb()
                            result.addSource(dataSource) { newData ->
                                result.value = creator.next(newData, payloadBack, false)
                                NLog.i(prefixLog, "Load from database (new): $newData")
                            }

                        }
                    }
                }
            } else {
                NLog.e(
                        prefixLog,
                        "CreateCall fail: [${response?.method}] ${response?.url} message: ${response?.error?.message}"
                      )
                onFetchFailed(response?.error)
                result.addSource(dbSource) { data ->
                    result.setValue(
                            creator.error( response?.error, payloadBack ) )
                }
            }
        }
    }


    fun toLiveData(): LiveData<ResourceType> {
        return result
    }


}
