package com.existing.boilerx.core.base.mvvm.view.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.existing.boilerx.core.BuildConfig
import com.existing.boilerx.core.base.delegate.FragmentDialogHelper
import com.existing.boilerx.core.base.delegate.FragmentDialogHelperDelegate
import com.existing.boilerx.core.base.delegate.RxDelegation
import com.existing.boilerx.core.base.mvvm.view.OnFragmentDialogListener
import com.existing.boilerx.core.exception.NotSetupLayoutException
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Created by「 The Khaeng 」on 08 Oct 2017 :)
 */
abstract class BaseDialogFragment
    : androidx.fragment.app.DialogFragment(), FragmentDialogHelper {

    companion object {
        private const val NO_REQUEST = -1
        private const val KEY_REQUEST_CODE = "key_request_code"
    }

    private var isDestroy = false
    private var listener: OnFragmentDialogListener? = null
    private val rxDelegation = RxDelegation()
    private lateinit var helper: FragmentDialogHelperDelegate

    var isRestoreInstanceStated: Boolean = false
    var enableLog = false


    override
    fun onCreate(savedInstanceState: Bundle?) {
        helper = FragmentDialogHelperDelegate(this)
        log("onPreCreate: savedInstanceState=$savedInstanceState")
        onPreCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        isDestroy = false
        val bundle = arguments
        if (bundle != null) {
            log("onArguments: arguments=$bundle")
            onArguments(bundle, savedInstanceState)
        }
        if (savedInstanceState != null) {
            log("onRestoreInstanceStates: savedInstanceState=$savedInstanceState")
            onRestoreInstanceStates(savedInstanceState)
        }
    }


    override
    fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            this.listener = context as OnFragmentDialogListener?
        } catch (e: ClassCastException) {
            Timber.w("$context.toString() isn't implement OnCompleteListener")
        }

    }

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        isCancelable = true
        dialog?.setOnCancelListener {
            setResultCode(Activity.RESULT_CANCELED)
            dismiss()
        }
        if (setupLayoutView() == 0) throw NotSetupLayoutException()
        log("setupLayoutView()")
        return inflater.inflate(setupLayoutView(), container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val intent = requireActivity().intent
        if (intent != null) {
            val extras = intent.extras
            log("onActivityIntentExtras: extras=$extras")
            onActivityIntentExtras(extras, savedInstanceState)
        }
        super.onViewCreated(view, savedInstanceState)
        log("onBindView: ")
        onBindView(view)
        if (BuildConfig.DEBUG) {
            log("onDebuggingMode: ")
            onDebuggingMode()
        }
        log("onInitialize: ")
        onInitialize(savedInstanceState)
        if (savedInstanceState == null) {
            log("onPrepareInstance: ")
            onPrepareInstance()
        }
        onSetupView()
        if (savedInstanceState != null) {
            log("onRestoreViewStates: savedInstanceState=$savedInstanceState")
            onRestoreViewStates(savedInstanceState)
        }
    }

    override
    fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        helper.apply {
            if (requestCode != NO_REQUEST) {
                listener?.onFragmentDialogResult(requestCode, getResultCode(), data)
            }
        }
    }

    override
    fun onStart() {
        log("onStart: ")
        super.onStart()
    }

    override
    fun onResume() {
        log("onResume: ")
        super.onResume()
    }

    override
    fun onPause() {
        log("onPause: ")
        super.onPause()
    }

    override
    fun onStop() {
        log("onStop: ")
        super.onStop()
    }


    /**
     * If Parent view is destroyed it will be calling onDestroy() before onFragmentDialogResult()
     */
    override
    fun onDestroy() {
        super.onDestroy()
        isDestroy = true
        rxDelegation.clearAllDisposables()
    }


    fun addDisposable(d: Disposable) {
        rxDelegation.addDisposable(d)
    }


    override
    fun setResultData(data: Bundle?) {
        helper.data = data
    }


    override
    fun setResultCode(resultCode: Int) {
        helper.setResultCode(resultCode)
    }

    @Deprecated(
            "use onSaveInstanceStates(Bundle)",
            ReplaceWith("fun onSaveInstanceState(outState: Bundle?)"),
            DeprecationLevel.WARNING)
    override
    fun onSaveInstanceState(outState: Bundle) {
        log("saveInstanceState: oustState=$outState")
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_REQUEST_CODE, helper.requestCode)
        onSaveInstanceStates(outState)
    }


    override
    fun show(target: androidx.fragment.app.Fragment, requestCode: Int) {
        helper.show(target, requestCode)
    }

    override
    fun show(activity: androidx.fragment.app.FragmentActivity, requestCode: Int) {
        helper.show(activity, requestCode)
    }

    override
    fun dismiss() {
        helper.dismiss()
    }

    override
    fun dismiss(resultCode: Int, extras: Bundle?) {
        helper.dismiss(resultCode,extras)
    }


    open fun onPreCreate(savedInstanceState: Bundle?) {}

    abstract fun setupLayoutView(): Int

    open fun onActivityIntentExtras(extras: Bundle?, savedInstanceState: Bundle?) {}

    open fun onDebuggingMode() {}

    open fun onArguments(bundle: Bundle, savedInstanceState: Bundle?) {}


    open fun onBindView(view: View?) {}

    open fun onInitialize(savedInstanceState: Bundle?) {
    }

    open fun onSetupView() {}

    open fun onRestoreInstanceStates(savedInstanceState: Bundle) {
        this.isRestoreInstanceStated = true
        helper.requestCode = savedInstanceState.getInt(KEY_REQUEST_CODE, -1)
    }

    open fun onSaveInstanceStates(outState: Bundle){}

    open fun onRestoreViewStates(savedInstanceState: Bundle) {}


    open fun onPrepareInstance() {}

    private fun log(message: String) {
        if (enableLog && BuildConfig.DEBUG) {
            Timber.d(message)
        }
    }

}