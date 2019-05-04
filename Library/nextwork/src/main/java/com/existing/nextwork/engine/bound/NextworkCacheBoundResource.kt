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
        private val onShouldFetch: (List<ResultType?>) -> Boolean,
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
                                              emitter.onNext(creator.loadingFromDatabase(payloadBack))
                                              val dataList = mutableListOf<ResultType>()
                                              loadFromDb()
                                                      .subscribeOn(Schedulers.computation())
                                                      .subscribe(object : FlowableSubscriber<ResultType> {
                                                          override
                                                          fun onSubscribe(s: Subscription) {
                                                              s.request(java.lang.Long.MAX_VALUE)
                                                          }

                                                          override
                                                          fun onComplete() {
                                                              val shouldFetch = onShouldFetch(dataList)
                                                              NLog.i(prefixLog, "ShouldFetch: $shouldFetch")
                                                              dataList.clear()
                                                              if (shouldFetch) {
                                                                  fetchNetwork(emitter)
                                                              } else {
                                                                  emitter.onNext(creator.completed(payloadBack))
                                                                  emitter.onComplete()
                                                              }
                                                          }

                                                          override
                                                          fun onNext(data: ResultType) {
                                                              dataList.add(data)
                                                              NLog.i(prefixLog, "Load from database (old): $data")
                                                              emitter.onNext(creator.next(data, payloadBack, true))
                                                          }

                                                          override
                                                          fun onError(e: Throwable) {
                                                              emitter.onError(e)
                                                          }

                                                      })


                                          }, BackpressureStrategy.BUFFER)

    private fun fetchNetwork(emitter: FlowableEmitter<ResourceType>) {
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        val api = createCall()
        emitter.onNext(creator.loadingFromNetwork(payloadBack))
        api.subscribeOn(Schedulers.computation())
                .subscribe(object : FlowableSubscriber<ResponseApiType> {
                    override
                    fun onSubscribe(s: Subscription) {
                        s.request(java.lang.Long.MAX_VALUE)
                    }

                    override
                    fun onComplete() {
                        appExecutors.mainThread.execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            loadFromDb()
                                    .subscribeOn(Schedulers.computation())
                                    .subscribe(object : FlowableSubscriber<ResultType> {
                                        override
                                        fun onSubscribe(s: Subscription) {
                                            s.request(java.lang.Long.MAX_VALUE)
                                        }

                                        override
                                        fun onComplete() {
                                            emitter.onNext(creator.completed(payloadBack))
                                            emitter.onComplete()
                                        }

                                        override
                                        fun onNext(newData: ResultType) {
                                            emitter.onNext(creator.next(newData, payloadBack, false))
                                            NLog.i(prefixLog, "Load from database (new): $newData")
                                        }

                                        override
                                        fun onError(e: Throwable) {
                                            emitter.onError(e)
                                        }

                                    })

                        }
                    }

                    override
                    fun onNext(response: ResponseApiType) {
                        NLog.i(prefixLog, "CreateCall next: [${response.method}] ${response.url}")
                        if (response.isSuccessful()) {
                            appExecutors.diskIO.execute {
                                val responseResult = onProcessResponse(response)
                                if (responseResult != null) {
                                    val convertData = onConvertToResultType(responseResult)
                                    onSaveCallResult(convertData)
                                    NLog.i(prefixLog, "Save call result: $convertData")
                                }
                            }
                        } else {
                            NLog.e(
                                    prefixLog,
                                    "CreateCall fail: [${response.method}] ${response.url} message: ${response.error?.message}"
                                  )
                            onFetchFailed(response.error)
                            emitter.onNext(creator.error(response.error, payloadBack))
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
