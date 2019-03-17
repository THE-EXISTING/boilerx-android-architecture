package com.existing.boilerx.core.base.mvvm.viewModel.bus.rx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


/**
 * Created by「 The Khaeng 」on 18 Sep 2017 :)
 */

class EventBusLiveData {
    private var livedata: MutableLiveData<Any> = MutableLiveData()

    fun post(obj: Any) {
        livedata.value = obj
    }

    fun newSubscribe(lifecycle: LifecycleOwner,
                     observer: Observer<Any>) {
        livedata = MutableLiveData()
        subscribe(lifecycle, observer)
    }

    fun subscribe(lifecycle: LifecycleOwner,
                  observer: Observer<Any>) {
        livedata.observe(lifecycle, observer)
    }


}
