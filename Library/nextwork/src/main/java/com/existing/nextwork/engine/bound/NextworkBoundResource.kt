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
open class NextworkBoundResource<
        ResultType,
        RequestType,
        ResponseApiType : ResponseWrapper<RequestType>,
        ResourceType : ResultWrapper<ResultType>>
@MainThread
constructor(
    private val appExecutors: AppExecutors,
    private val creator: NextworkResourceCreator<ResultType, ResourceType>,
    @MainThread
    private val createCall: () -> Flowable<ResponseApiType>,
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
    private val payloadBack: Any? = null,
    private val prefixLog: String = ""
) {


    private var result: Flowable<ResourceType> =
        Flowable.create<ResourceType>({ emitter ->
            fetchNetwork(emitter)
        }, BackpressureStrategy.BUFFER)

    private fun fetchNetwork(emitter: FlowableEmitter<ResourceType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        emitter.onNext(creator.loadingFromNetwork(null, payloadBack, true))
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
                                val convertData = onConvertToResultType(responseResult)
                                onSaveCallResult(convertData)
                                NLog.i(prefixLog, "Save call result: $convertData")
                                appExecutors.mainThread.execute {
                                    emitter.onNext(
                                        creator.success(
                                            convertData,
                                            payloadBack,
                                            false,
                                            true
                                        )
                                    )

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
                                null,
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
