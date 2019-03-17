package com.existing.nextwork.factory

import androidx.lifecycle.LiveData
import com.existing.nextwork.engine.model.ResponseWrapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response

import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

internal class NextworkCallAdapter<R>(
    private val responseType: Type,
    private val isLiveData: Boolean,
    private val isFlowable: Boolean,
    private val isSingle: Boolean,
    private val isMaybe: Boolean,
    private val isCompletable: Boolean
) : CallAdapter<R, Any> {

    override
    fun responseType(): Type {
        return responseType
    }

    override
    fun adapt(call: Call<R>): Any {
        if (isLiveData) {
            return object : LiveData<ResponseWrapper<R>>() {
                val started = AtomicBoolean(false)
                override
                fun onActive() {
                    super.onActive()
                    if (started.compareAndSet(false, true)) {
                        call.enqueue(object : Callback<R> {
                            override
                            fun onResponse(call: Call<R>, response: Response<R>) {
                                postValue(ResponseWrapper(response))
                            }

                            override
                            fun onFailure(call: Call<R>, throwable: Throwable) {
                                postValue(ResponseWrapper(throwable))
                            }
                        })
                    }
                }
            }
        } else {
            val responseObservable = Observable.create<ResponseWrapper<R>> { emitter ->
                try {
                    val started = AtomicBoolean(false)
                    if (started.compareAndSet(false, true)) {
                        call.enqueue(object : Callback<R> {
                            override
                            fun onResponse(call1: Call<R>, response: Response<R>) {
                                emitter.onNext(ResponseWrapper(response))
                                emitter.onComplete()
                            }

                            override
                            fun onFailure(call1: Call<R>, throwable: Throwable) {
                                emitter.onError(throwable)
                            }
                        })
                    }
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }

            if (isFlowable) {
                return responseObservable.toFlowable(BackpressureStrategy.LATEST)
            }
            if (isSingle) {
                return responseObservable.singleOrError()
            }
            if (isMaybe) {
                return responseObservable.singleElement()
            }
            if (isCompletable) {
                return responseObservable.ignoreElements()
            }
            return responseObservable
        }
    }
}
