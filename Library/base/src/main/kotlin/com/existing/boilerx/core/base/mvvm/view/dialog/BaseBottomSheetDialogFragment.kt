package com.existing.boilerx.core.base.mvvm.view.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
abstract class BaseBottomSheetDialogFragment
    : BottomSheetDialogFragment(), FragmentDialogHelper {

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

    @SuppressLint("RestrictedApi")
    override
    fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val layoutResId = setupLayoutView()
        if (setupLayoutView() == 0) throw NotSetupLayoutException()
        val contentView = View.inflate(context, layoutResId, null)
        setContentView(contentView)

        val params = (contentView.parent as View).layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override
                fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

                override
                fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
        }


        dialog.setOnCancelListener { _ ->
            setResultCode(Activity.RESULT_CANCELED)
            dismiss()
        }

        onBindView(contentView)
        if (BuildConfig.DEBUG) {
            log("onDebuggingMode: ")
            onDebuggingMode()
        }
        log("onInitialize: ")
        onInitialize()
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            log("onSetupView: behavior: $behavior")
            onSetupView(behavior)
        } else {
            log("onSetupView: behavior: null")
            onSetupView(null)
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


    override
    fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        helper.apply {
            if (requestCode != NO_REQUEST) {
                listener?.onFragmentDialogResult(requestCode, getResultCode(), data)
            }
        }
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

    override
    fun onSaveInstanceState(outState: Bundle) {
        log("saveInstanceState: oustState=$outState")
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_REQUEST_CODE, helper.requestCode)
        onSaveInstanceStates(outState)
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
        helper.dismiss(resultCode, extras)
    }


    open fun setContentView(contentView: View) {
        dialog?.setContentView(contentView)
    }

    open fun onPreCreate(savedInstanceState: Bundle?) {}

    abstract fun setupLayoutView(): Int

    open fun onDebuggingMode() {}

    open fun onArguments(bundle: Bundle, savedInstanceState: Bundle?) {}


    open fun onBindView(view: View?) {}

    open fun onInitialize() {
    }

    open fun onSetupView(behavior: BottomSheetBehavior<*>?) {}

    open fun onRestoreInstanceStates(savedInstanceState: Bundle) {
        this.isRestoreInstanceStated = true
        helper.requestCode = savedInstanceState.getInt(KEY_REQUEST_CODE, -1)
    }

    open fun onSaveInstanceStates(outState: Bundle) {}


    private fun log(message: String) {
        if (enableLog && BuildConfig.DEBUG) {
            Timber.d(message)
        }
    }

}