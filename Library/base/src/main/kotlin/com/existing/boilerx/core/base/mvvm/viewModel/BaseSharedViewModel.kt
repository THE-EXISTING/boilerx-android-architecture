package com.existing.boilerx.core.base.mvvm.viewModel


import androidx.lifecycle.Observer
import com.existing.boilerx.core.base.mvvm.viewModel.bus.rx.EventBusLiveData


/**
 * Created by「 The Khaeng 」on 23 Aug 2017 :)
 */

abstract class BaseSharedViewModel : BaseViewModel() {
    companion object {
        private var busViewModel: EventBusLiveData = EventBusLiveData()
    }


    fun postEvent(obj: Any) {
        busViewModel.post(obj)
    }

    fun subscribeNewBus(activity: androidx.fragment.app.FragmentActivity,
                        observer: Observer<Any>) {
        busViewModel.newSubscribe(activity, observer)

    }

    fun subscribeBus(fragment: androidx.fragment.app.FragmentActivity,
                     observer: Observer<Any>) {
        busViewModel.subscribe(fragment, observer)
    }

    fun subscribeBus(fragment: androidx.fragment.app.Fragment,
                     observer: Observer<Any>) {
        busViewModel.subscribe(fragment, observer)
    }

}
