package com.existing.boilerx.core.base.delegate

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * Created by「 The Khaeng 」on 31 Aug 2017 :)
 */

class ActivityHelperDelegate
    : ActivityHelper {

    private var fragment: WeakReference<androidx.fragment.app.Fragment>? = null
    private var activity: WeakReference<Activity>? = null

    constructor(activity: androidx.fragment.app.FragmentActivity) {
        this.activity = WeakReference(activity)
    }

    constructor(fragment: androidx.fragment.app.Fragment) {
        this.fragment = WeakReference(fragment)
    }

    fun createTransaction(): OpenActivityTransaction {
        return OpenActivityTransaction()
    }

    override
    fun openActivity(targetClass: Class<*>,
                     request: Int,
                     data: Bundle?,
                     flags: Int) {
        val openActivityTransaction = createTransaction()
                .setBundle(data)
                .setRequestCode(request)
                .setFlags(flags)
        open(openActivityTransaction, targetClass)
    }


    override
    fun openActivityWithFinish(targetClass: Class<*>, data: Bundle?) {
        val openActivityTransaction = createTransaction()
                .setBundle(data)
                .setFinish(true)
        open(openActivityTransaction, targetClass)
    }


    override
    fun openActivityWithAllFinish(targetClass: Class<*>, data: Bundle?) {
        val openActivityTransaction = createTransaction()
                .setBundle(data)
                .setFinishAllPrevious(true)
        open(openActivityTransaction, targetClass)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyView() {
        activity = null
        fragment = null
    }

    /* =========================== Private method ============================================= */
    private fun open(openActivityTransaction: OpenActivityTransaction, targetClass: Class<*>) {
        when {
            getActivity() != null -> openActivityTransaction.open(getActivity(), targetClass)
            getFragment() != null -> openActivityTransaction.open(getFragment(), targetClass)
            else -> throw RuntimeException("Bind Activity or Fragment before.")
        }
    }


    private fun getActivity(): Activity? {
        return activity?.get()
    }

    private fun getFragment(): androidx.fragment.app.Fragment? {
        return fragment?.get()
    }
}
