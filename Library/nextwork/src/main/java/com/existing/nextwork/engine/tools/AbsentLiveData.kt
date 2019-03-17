package com.existing.nextwork.engine.tools

import androidx.lifecycle.LiveData

/**
 * Created by「 The Khaeng 」on 05 Jan 2019 :)
 */
class AbsentLiveData<T> private constructor() : LiveData<T>() {

    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}
