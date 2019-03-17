package com.existing.boilerx.core.base.mvvm.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.existing.boilerx.core.base.delegate.RxDelegation
import com.existing.boilerx.core.base.mvvm.viewModel.BaseViewModel
import io.reactivex.disposables.Disposable

/**
 * Created by「 The Khaeng 」on 27 Aug 2017 :)
 */

abstract class BaseMvvmActivity : BaseActivity() {

    private val rxDelegation = RxDelegation()


    override
    fun onPreCreate(savedInstanceState: Bundle?) {
        super.onPreCreate(savedInstanceState)
        onSetupViewModel(savedInstanceState)
    }

    open fun onSetupViewModel(savedInstanceState: Bundle?) {

    }

    override
    fun onDestroy() {
        super.onDestroy()
        rxDelegation.clearAllDisposables()
    }

    fun <VM : BaseViewModel> getViewModel(viewModelClass: Class<VM>): VM = ViewModelProviders
            .of(this)
            .get(viewModelClass)

    fun <VM : BaseViewModel> getViewModel(key: String, viewModelClass: Class<VM>): VM = ViewModelProviders
            .of(this)
            .get(key, viewModelClass)


    fun addDisposable(d: Disposable) {
        rxDelegation.addDisposable(d)
    }

}

