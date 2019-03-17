package com.existing.boilerx.core.base.delegate

import android.app.Activity
import android.os.Bundle
import com.existing.boilerx.core.base.mvvm.view.OnFragmentDialogListener


class FragmentDialogHelperDelegate(val dialog: androidx.fragment.app.DialogFragment) : FragmentDialogHelper {

    companion object {
        private const val NO_REQUEST = -1
        private const val KEY_REQUEST_CODE = "key_request_code"
    }

    private var isDismissWithResult = false

    var requestCode = NO_REQUEST
    var data: Bundle? = null
    private var resultCode = Activity.RESULT_CANCELED


    override
    fun show(target: androidx.fragment.app.Fragment, requestCode: Int) {
        this.requestCode = requestCode
        dialog.setTargetFragment(target, requestCode)
        target.fragmentManager?.let{dialog.show(it, target.tag)}
    }

    override
    fun show(activity: androidx.fragment.app.FragmentActivity, requestCode: Int) {
        this.requestCode = requestCode
        dialog.show(activity.supportFragmentManager, activity.javaClass.simpleName)
    }


    override
    fun setResultData(data: Bundle?) {
        this.data = data
    }


    override
    fun setResultCode(resultCode: Int) {
        this.resultCode = resultCode
    }


    override
    fun dismiss() {
        if (requestCode != NO_REQUEST) {
            dismiss(resultCode, null)
        } else {
            dialog.dismiss()
        }
    }

    override
    fun dismiss(resultCode: Int, extras: Bundle?) {
        if (requestCode != NO_REQUEST) {
            isDismissWithResult = true
            withResult(resultCode, extras)
            dialog.dismiss()
        } else {
            dialog.dismiss()
        }
    }


    fun getResultCode(): Int = resultCode

    @Suppress("NAME_SHADOWING")
    private fun withResult(resultCode: Int, data: Bundle?) {
        setResultCode(resultCode)
        setResultData(data)
        val fragment = dialog.targetFragment
        if (fragment != null && fragment is OnFragmentDialogListener) {
            fragment.onFragmentDialogResult(requestCode, resultCode, data)
        }
    }


}
