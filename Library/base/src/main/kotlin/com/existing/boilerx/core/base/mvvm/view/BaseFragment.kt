package com.existing.boilerx.core.base.mvvm.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import com.existing.boilerx.core.BuildConfig
import com.existing.boilerx.core.base.delegate.*
import com.existing.boilerx.core.base.utils.ScreenOrientationHelper
import com.existing.boilerx.core.exception.NotSetupLayoutException
import com.existing.boilerx.ktx.notnull
import timber.log.Timber

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

abstract class BaseFragment
    : Fragment(),
      ActivityHelper,
      FragmentHelper,
      ScreenOrientationHelper.ScreenOrientationChangeListener {

    private val helper = ScreenOrientationHelper()
    private lateinit var activityOpener: ActivityHelperDelegate
    private lateinit var fragmentDelegate: FragmentHelperDelegate
    private var tmpWindowsInset: WindowInsets? = null

    var isRestoreInstanceStated: Boolean = false
    var enableLog = false

    override
    fun onCreate(savedInstanceState: Bundle?) {
        activityOpener = ActivityHelperDelegate(this)
        activity?.let {
            fragmentDelegate = FragmentHelperDelegate(it)
            fragmentDelegate.setCanCommit(false)
        }
        log("onPreCreate: savedInstanceState=$savedInstanceState")
        onPreCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            log("onArguments: arguments=$bundle")
            onArguments(bundle)
        }

        if (savedInstanceState != null) {
            log("onRestoreInstanceStates: savedInstanceState=$savedInstanceState")
            onRestoreInstanceStates(savedInstanceState)
        }
    }

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutResId = setupLayoutView()
        if (setupLayoutView() == 0) throw NotSetupLayoutException()
        log("setupLayoutView()")
        return inflater.inflate(layoutResId, container, false)
    }


    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            tmpWindowsInset?.let {
                view.onApplyWindowInsets(it)
                tmpWindowsInset = null
            }
        }
        val intent = requireActivity().intent
        if (intent != null) {
            val extras = intent.extras
            log("onIntentExtras: extras=$extras")
            onActivityIntentExtras(extras, savedInstanceState)
        }
        super.onViewCreated(view, savedInstanceState)
        log("onBindView")
        onBindView(view)

        if (BuildConfig.DEBUG) {
            log("onDebuggingMode: ")
            onDebuggingMode()
        }
        Pair(activity, savedInstanceState).notnull { activity, savedState ->
            helper.setActivity(activity)
            helper.onCreate(savedState)
            helper.setScreenOrientationChangeListener(this)
            helper.onRestoreInstanceState(savedState)
            helper.checkOrientation()
        }
        log("onInitialize")
        onInitialize(savedInstanceState)
        if (savedInstanceState == null) {
            log("onPrepareInstanceState")
            onPrepareInstance()
        }
        log("onSetupView")
        onSetupView(savedInstanceState)
        if (savedInstanceState != null) {
            log("onRestoreViewStates: savedInstanceState=$savedInstanceState")
            onRestoreViewStates(savedInstanceState)
        }
    }

    @Suppress("NAME_SHADOWING")
    override
    fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        fragmentDelegate.setCanCommit(true)
        fragmentDelegate.openFragmentInStackIfAvailable()
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
        fragmentDelegate.setCanCommit(false)
        fragmentDelegate.openFragmentStack = null
    }

    @Deprecated(
            "use onSaveInstanceStates(Bundle)",
            ReplaceWith("fun onSaveInstanceState(outState: Bundle?)"),
            DeprecationLevel.WARNING)
    override
    fun onSaveInstanceState(outState: Bundle) {
        log("saveInstanceState: oustState=$outState")
        super.onSaveInstanceState(outState)
        helper.onSaveInstanceState(outState)
        onSaveInstanceStates(outState)
    }

    open fun setWindowsInsets(insets: WindowInsets?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            tmpWindowsInset = insets
            if (tmpWindowsInset != null && view != null) {
                view?.onApplyWindowInsets(insets)
                tmpWindowsInset = null
            }
        }
    }

    open fun createActivityTransaction(): OpenActivityTransaction {
        return activityOpener.createTransaction()
    }

    open fun createFragmentTransaction(): OpenFragmentTransaction {
        return fragmentDelegate.createTransaction()
    }


    override
    fun onScreenOrientationChanged(orientation: Int) {

    }

    override
    fun getCurrentFragment(): Fragment? = fragmentDelegate.getCurrentFragment()

    override
    fun onScreenOrientationChangedToPortrait() {

    }

    override
    fun onScreenOrientationChangedToLandscape() {

    }

    open fun onBackPressed(): Boolean {
        return false
    }

    abstract fun setupLayoutView(): Int

    open fun onArguments(bundle: Bundle) {
        log("onIntentExtras: bundle=$bundle")
    }

    open fun onPreCreate(savedInstanceState: Bundle?) {}

    open fun onActivityIntentExtras(extras: Bundle?, savedInstanceState: Bundle?) {}

    open fun onDebuggingMode() {}

    open fun onBindView(view: View?) {}

    open fun onInitialize(savedInstanceState: Bundle?) {}

    open fun onSetupView(savedInstanceState: Bundle?) {}

    open fun onPrepareInstance() {}

    open fun onSaveInstanceStates(outState: Bundle) {}

    open fun onRestoreInstanceStates(savedInstanceState: Bundle) {
        isRestoreInstanceStated = true
    }

    open fun onRestoreViewStates(savedInstanceState: Bundle) {
    }


    /* ============================ Open Activity ======================================== */
    override
    fun openActivity(targetClass: Class<*>, request: Int, data: Bundle?, flags: Int) {
        activityOpener.openActivity(targetClass, request, data, flags)
    }

    override
    fun openActivityWithFinish(targetClass: Class<*>, data: Bundle?) {
        activityOpener.openActivityWithFinish(targetClass, data)
    }

    override
    fun openActivityWithAllFinish(targetClass: Class<*>, data: Bundle?) {
        activityOpener.openActivityWithAllFinish(targetClass, data)
    }


    /* ============================ Open Fragment ======================================== */
    override
    fun bindFragmentContainer(containerLayoutRes: Int) {
        (activity as? BaseActivity)?.bindFragmentContainer(containerLayoutRes)
    }

    override
    fun initFragment(fragment: Fragment, savedInstanceState: Bundle?) {
        (activity as? BaseActivity)?.initFragment(fragment, savedInstanceState)
    }

    override
    fun initFragment(containerLayoutRes: Int, fragment: Fragment, savedInstanceState: Bundle?) {
        (activity as? BaseActivity)?.initFragment(containerLayoutRes, fragment, savedInstanceState)
    }

    override
    fun containFragment(tag: String): Boolean {
        return (activity as? BaseActivity)?.containFragment(tag) ?: false
    }

    override
    fun clearBackStackFragmentTo(index: Int) {
        (activity as? BaseActivity)?.clearBackStackFragmentTo(index)
    }

    override
    fun clearAllBackStacksFragment() {
        (activity as? BaseActivity)?.clearAllBackStacksFragment()
    }

    override
    fun backPressedFragment() {
        (activity as? BaseActivity)?.backPressedFragment()
    }

    override
    fun backPressedToFragment(tag: String) {
        (activity as? BaseActivity)?.backPressedToFragment(tag)
    }

    override
    fun openFragmentSlideFromLeft(fragment: Fragment, withFinish: Boolean) {
        (activity as? BaseActivity)?.openFragmentSlideFromLeft(fragment, withFinish)
    }

    override
    fun openFragmentSlideFromRight(fragment: Fragment, withFinish: Boolean) {
        (activity as? BaseActivity)?.openFragmentSlideFromRight(fragment, withFinish)
    }


    override
    fun openFragment(fragment: Fragment, withFinish: Boolean) {
        (activity as? BaseActivity)?.openFragment(fragment, withFinish)
    }


    private fun log(message: String) {
        if (enableLog && BuildConfig.DEBUG) {
            Timber.d(message)
        }
    }


}

