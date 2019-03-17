package com.existing.boilerx.core.base.usecase

import androidx.lifecycle.MediatorLiveData

/**
 * Created by「 The Khaeng 」on 14 Jan 2019 :)
 */
abstract class UseCaseLiveData<PARAMETER, RESULT> : BaseUseCase() {

    protected val result = MediatorLiveData<RESULT>()

    // Make this as open so that mock instances can mock this method
    open fun observe(): MediatorLiveData<RESULT> {
        return result
    }

    abstract fun execute(parameters: PARAMETER, liveData: MediatorLiveData<RESULT> = this.result)
}
