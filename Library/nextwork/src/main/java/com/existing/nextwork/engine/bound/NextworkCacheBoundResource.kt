package com.existing.nextwork.engine.bound


import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.existing.nextwork.engine.AppExecutors
import com.existing.nextwork.engine.NextworkResourceCreator
import com.existing.nextwork.engine.model.ResponseWrapper
import com.existing.nextwork.engine.model.ResultWrapper
import com.existing.nextwork.operator.NLog
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableSubscriber
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
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
open class NextworkCacheBoundResource<
        ResultType,
        RequestType,
        ResponseApiType : ResponseWrapper<RequestType>,
        ResourceType : ResultWrapper<ResultType>>
@MainThread
constructor(
    private val appExecutors: AppExecutors,
    private val creator: NextworkResourceCreator<ResultType, ResourceType>,
    @MainThread
    private val loadFromDb: () -> Flowable<ResultType>,
    @MainThread
    private val onShouldFetch: (ResultType?) -> Boolean,
    @MainThread
    private val createCall: () -> Flowable<ResponseApiType>,
    private val onConvertToResultType: (ResultType?, RequestType) -> ResultType,
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
    private val prefixLog: String = ""
) {


    private var result: Flowable<ResourceType> =
        Flowable.create<ResourceType>({ emitter ->
            emitter.onNext(creator.loadingFromDatabase(payloadBack))
            loadFromDb()
                // Read from disk on Computation Scheduler
                .subscribeOn(Schedulers.computation())
                .subscribe(object : FlowableSubscriber<ResultType> {
                    override
                    fun onSubscribe(s: Subscription) {
                        s.request(java.lang.Long.MAX_VALUE)
                    }

                    override
                    fun onComplete() {
                    }

                    override
                    fun onNext(data: ResultType) {
                        NLog.i(prefixLog, "Load from database (old): $data")
                        val shouldFetch = onShouldFetch(data)
                        NLog.i(prefixLog, "ShouldFetch: $shouldFetch")
                        if (shouldFetch) {
                            if (isLoadCacheBeforeFetch)
                                emitter.onNext(creator.success(data, payloadBack, true, true))
                            fetchNetwork(emitter, data)
                        } else {
                            emitter.onNext(
                                creator.success(
                                    data,
                                    payloadBack,
                                    true,
                                    false
                                )
                            )
                            emitter.onComplete()
                        }

                    }

                    override
                    fun onError(e: Throwable) {
                        emitter.onError(e)
                    }

                })


        }, BackpressureStrategy.BUFFER)

    private fun fetchNetwork(emitter: FlowableEmitter<ResourceType>, oldData: ResultType) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        emitter.onNext(creator.loadingFromNetwork(oldData, payloadBack, true))
        apiResponse
            .subscribe(object : FlowableSubscriber<ResponseApiType> {
                override
                fun onSubscribe(s: Subscription) {
                    s.request(java.lang.Long.MAX_VALUE)
                }

                override
                fun onComplete() {
                }

                override
                fun onNext(response: ResponseApiType) {
                    NLog.i(prefixLog, "CreateCall success: [${response.method}] ${response.url}")
                    if (response.isSuccessful()) {
                        appExecutors.diskIO.execute {
                            val responseResult = onProcessResponse(response)
                            if (responseResult != null) {
                                val convertData = onConvertToResultType(oldData, responseResult)
                                onSaveCallResult(convertData)
                                NLog.i(prefixLog, "Save call result: $convertData")
                                appExecutors.mainThread.execute {
                                    // we specially request a new live data,
                                    // otherwise we will get immediately last cached value,
                                    // which may not be updated with latest results received from network.
                                    loadFromDb()
                                        .subscribeOn(Schedulers.computation())
                                        .subscribe { newData ->
                                            emitter.onNext(
                                                creator.success(
                                                    newData,
                                                    payloadBack,
                                                    false,
                                                    true
                                                )
                                            )
                                            emitter.onComplete()
                                            NLog.i(prefixLog, "Load from database (new): $newData")
                                        }

                                }
                            }
                        }
                    } else {
                        NLog.e(
                            prefixLog,
                            "CreateCall fail: [${response.method}] ${response.url} message: ${response.error?.message}"
                        )
                        onFetchFailed(response.error)
                        emitter.onNext(
                            creator.error(
                                response.error,
                                oldData,
                                payloadBack,
                                true
                            )
                        )
                        emitter.onComplete()
                    }

                }


                override
                fun onError(e: Throwable) {
                    emitter.onError(e)
                }

            })

    }


    fun toFlowable(): Flowable<ResourceType> {
        return result
    }


}
