package com.existing.boilerx.core.base.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.existing.boilerx.core.base.mvvm.view.dialog.BaseDialogFragment

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

abstract class BaseMvvmDialogFragment
    : BaseDialogFragment(){

    override
    fun onCreate(savedInstanceState: Bundle?) {
        onOneTimeSetupViewModel(savedInstanceState)
        super.onCreate(savedInstanceState)
    }


    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onSetupViewModel(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    open fun onOneTimeSetupViewModel(savedInstanceState: Bundle?) {

    }

    open fun onSetupViewModel(savedInstanceState: Bundle?) {

    }


    fun <VM : ViewModel> getViewModel(viewModelClass: Class<VM>): VM
            = ViewModelProviders.of(this).get(viewModelClass)

    fun <VM : ViewModel> getViewModel(key: String, viewModelClass: Class<VM>): VM
            = ViewModelProviders.of(this).get(key, viewModelClass)

    fun <VM : ViewModel> getSharedViewModel(viewModelClass: Class<VM>): VM?
            = activity?.let { ViewModelProviders.of(it).get(viewModelClass) }


}