package com.existing.boilerx.core.base.mvvm.view.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import java.lang.ref.WeakReference


/**
 * Created by「 The Khaeng 」on 20 Sep 2017 :)
 */

abstract class BaseMvvmListAdapter<VH : BaseViewHolder>
    : BaseListAdapter<VH> {

    protected var activity: WeakReference<FragmentActivity>? = null
    protected var fragment: WeakReference<Fragment>? = null

    constructor(activity: FragmentActivity) : super() {
        this.activity = WeakReference(activity)
    }

    constructor(fragment: Fragment) : super() {
        this.fragment = WeakReference(fragment)
        fragment.activity?.let {
            this.activity = WeakReference(it)
        }
    }


    fun <VM : ViewModel> getViewModel(viewModelClass: Class<VM>): VM {
        return if (fragment != null) {
            ViewModelProviders.of(fragment?.get()!!).get(viewModelClass)
        } else {
            ViewModelProviders.of(activity?.get()!!).get(viewModelClass)
        }
    }

    fun <VM : ViewModel> getViewModel(key: String, viewModelClass: Class<VM>): VM {
        return if (fragment != null) {
            ViewModelProviders.of(fragment?.get()!!).get(key, viewModelClass)
        } else {
            ViewModelProviders.of(activity?.get()!!).get(key, viewModelClass)
        }
    }


    fun <VM : ViewModel> getSharedViewModel(viewModelClass: Class<VM>): VM? {
        if (activity == null) return null
        return ViewModelProviders.of(activity?.get()!!).get(viewModelClass)
    }

}
