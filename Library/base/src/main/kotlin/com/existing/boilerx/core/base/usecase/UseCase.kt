package com.existing.boilerx.core.base.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Created by「 The Khaeng 」on 14 Jan 2019 :)
 */
abstract class UseCase<in PARAMETER, RESULT> {

    /** Executes the use case asynchronously and places the [Result] in a MutableLiveData
     *
     * @param parameters the input parameters to run the use case with
     * @param result the MutableLiveData where the result is posted to
     *
     */
    operator fun invoke(parameters: PARAMETER, result: MutableLiveData<RESULT>) {
        execute(parameters).let { useCaseResult ->
            result.postValue(useCaseResult)
        }
    }

    /** Executes the use case asynchronously and returns a [Result] in a new LiveData object.
     *
     * @return an observable [LiveData] with a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    operator fun invoke(parameters: PARAMETER): LiveData<RESULT> {
        val liveCallback: MutableLiveData<RESULT> = MutableLiveData()
        this(parameters, liveCallback)
        return liveCallback
    }

    /** Executes the use case synchronously  */
    fun executeNow(parameters: PARAMETER): RESULT {
        return execute(parameters)
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract fun execute(parameter: PARAMETER): RESULT
}

operator fun <RESULT> UseCase<Unit, RESULT>.invoke(): LiveData<RESULT> = this(Unit)
operator fun <RESULT> UseCase<Unit, RESULT>.invoke(result: MutableLiveData<RESULT>) = this(Unit, result)
