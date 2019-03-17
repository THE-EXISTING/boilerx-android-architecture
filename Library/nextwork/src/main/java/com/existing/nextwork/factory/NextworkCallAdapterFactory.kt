package com.existing.nextwork.factory

import androidx.lifecycle.LiveData
import com.existing.nextwork.engine.model.ResponseWrapper
import io.reactivex.*
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NextworkCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        /**
         * Returns an instance which creates synchronous observables that do not operate on any scheduler
         * by default.
         */
        @JvmStatic
        fun create(): NextworkCallAdapterFactory {
            return NextworkCallAdapterFactory()
        }
    }


    override
    fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawType = CallAdapter.Factory.getRawType(returnType)

        if (rawType == Completable::class.java) {
            // Completable is not parameterized (which is what the rest of this method deals with) so it
            // can only be created with a single configuration.
            return NextworkCallAdapter<Any>(Void::class.java, false, false, false, false, true)
        }

        val isLiveData = rawType == LiveData::class.java
        val isFlowable = rawType == Flowable::class.java
        val isSingle = rawType == Single::class.java
        val isMaybe = rawType == Maybe::class.java
        val isObservable = rawType == Observable::class.java
        if (!isLiveData && !isObservable && !isFlowable && !isSingle && !isMaybe) {
            return null
        }


        val observableType = CallAdapter.Factory.getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = CallAdapter.Factory.getRawType(observableType)
        if (rawObservableType != ResponseWrapper::class.java) {
            throw IllegalArgumentException("type must be a ResponseWrapper")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        val bodyType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
        return NextworkCallAdapter<Any>(bodyType, isLiveData, isFlowable, isSingle, isMaybe, false)
    }

}
