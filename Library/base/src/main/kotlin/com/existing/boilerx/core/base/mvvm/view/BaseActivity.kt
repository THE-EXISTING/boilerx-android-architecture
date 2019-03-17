package com.existing.boilerx.core.base.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.existing.boilerx.core.BuildConfig
import com.existing.boilerx.core.base.delegate.*
import com.existing.boilerx.core.base.mvvm.view.adapter.BasePagerAdapter
import com.existing.boilerx.core.base.utils.ScreenOrientationHelper
import com.existing.boilerx.core.exception.NotSetupLayoutException
import timber.log.Timber


/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

abstract class BaseActivity
    : AppCompatActivity(),
      ActivityHelper,
      FragmentHelper,
      ScreenOrientationHelper.ScreenOrientationChangeListener {

    companion object {
        const val NO_LAYOUT = -2
    }

    private val helper = ScreenOrientationHelper()
    private lateinit var activityOpener: ActivityHelperDelegate
    private lateinit var fragmentDelegate: FragmentHelperDelegate
    private var viewPager: androidx.viewpager.widget.ViewPager? = null

    var isRestoreInstanceStated: Boolean = false
    var enableLog = false
    val rootView: View get() = window.decorView.rootView
    val decorView: View get() = window.decorView

    /**
     * This method is not single responsibility follow by SOLID mechanism. So, we split to 8 method.
     *
     * 1. [BaseActivity.onPreCreate]
     * 2. [BaseActivity.onIntentExtras] : Will be called only when activity have **intent.extras**
     * 3. [BaseActivity.setupLayoutView]
     * 4. [BaseActivity.onBindView]
     * 5. [BaseActivity.onDebuggingMode] : Will be called only when build variants is **debug mode**
     * 6. [BaseActivity.onInitialize]
     * 7. [BaseActivity.onPrepareInstanceState] : Will be called only when savedInstanceState is **null**
     * 8. [BaseActivity.onSetupView]
     *
     * @param savedInstanceState Bundle?
     */
    @Deprecated(
            "onCreate(...) method is not single responsibility follow by SOLID mechanism",
            level = DeprecationLevel.WARNING)
    override
    fun onCreate(savedInstanceState: Bundle?) {
        activityOpener = ActivityHelperDelegate(this)
        fragmentDelegate = FragmentHelperDelegate(this)
        helper.setActivity(this)
        helper.onCreate(savedInstanceState)
        helper.setScreenOrientationChangeListener(this)
        log("onPreCreate:")
        onPreCreate(savedInstanceState)
        if (intent != null) {
            val extras = intent.extras
            log("onIntentExtras: extras=$extras")
            onIntentExtras(extras, savedInstanceState)
        }
        super.onCreate(savedInstanceState)
        val layoutResId = setupLayoutView()
        if (layoutResId != NO_LAYOUT && setupLayoutView() == 0) throw NotSetupLayoutException()
        log("setContentView: ")
        if (layoutResId != NO_LAYOUT) {
            setContentView(layoutResId)
        }
        log("onBindView: ")
        onBindView(savedInstanceState)
        if (BuildConfig.DEBUG) {
            log("onDebuggingMode: ")
            onDebuggingMode(savedInstanceState)
        }
        log("onInitialize: ")
        onInitialize(savedInstanceState)
        if (savedInstanceState == null) {
            log("onPrepareInstanceState")
            onPrepareInstanceState()
        }
        log("onSetupView: ")
        onSetupView(savedInstanceState)
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
    fun onPostResume() {
        log("onPostResume: ")
        super.onPostResume()
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

    override
    fun onDestroy() {
        log("onDestroy: ")
        super.onDestroy()
    }

    /**
     * This method is **deprecated**
     *
     * @see onSaveInstanceStates(Bundle).
     *
     * @param outState Bundle?
     */
    @Deprecated(
            "use onSaveInstanceStates(Bundle)",
            ReplaceWith("fun onSaveInstanceState(outState: Bundle?)"),
            DeprecationLevel.WARNING)
    override
    fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        helper.onSaveInstanceState(outState)
        log("onSaveInstanceState: outState=$outState")
        onSaveInstanceStates(outState)
    }

    /**
     * This method is **deprecated**
     *
     * @see onRestoreInstanceStates(Bundle)
     * @see onRestoreViewStates(Bundle)
     *
     * @param savedInstanceState Bundle
     */
    @Deprecated("use onRestoreInstanceStates(Bundle)",
                ReplaceWith("fun onRestoreInstanceState(savedInstanceState: Bundle)"),
                DeprecationLevel.WARNING)
    override
    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isRestoreInstanceStated = true
        super.onRestoreInstanceState(savedInstanceState)
        helper.onRestoreInstanceState(savedInstanceState)
        log("onRestoreInstanceStates: savedInstanceState=$savedInstanceState")
        onRestoreInstanceStates(savedInstanceState)
        helper.checkOrientation()
        log("onRestoreViewStates: savedInstanceState=$savedInstanceState")
        onRestoreViewStates(savedInstanceState)
    }

    override
    fun onScreenOrientationChanged(orientation: Int) {
        log("onScreenOrientationChanged: orientation=$orientation")
    }

    override
    fun onScreenOrientationChangedToLandscape() {
        log("onScreenOrientationChangedToLandscape:")
    }

    override
    fun onScreenOrientationChangedToPortrait() {
        log("onScreenOrientationChangedToPortrait:")
    }

    override
    fun getCurrentFragment(): androidx.fragment.app.Fragment? = fragmentDelegate.getCurrentFragment()


    fun registerOnBackPressedViewPager(viewPager: androidx.viewpager.widget.ViewPager?) {
        this.viewPager = viewPager
    }

    override
    fun onBackPressed() {
        val isBackPressedFragment: Boolean =
                if (viewPager != null) {
                    val adapter = viewPager?.adapter
                    val fragment = when (adapter) {
                        is BasePagerAdapter -> adapter.fragmentList[viewPager!!.currentItem]
                        else -> null
                    }
                    (fragment as? BaseFragment)?.onBackPressed() ?: false
                } else {
                    supportFragmentManager.fragments
                            .filterIsInstance(BaseFragment::class.java)
                            .lastOrNull { it.isVisible }
                            ?.onBackPressed() ?: false
                }

        if (!supportFragmentManager.popBackStackImmediate() && !isBackPressedFragment) {
            super.onBackPressed()
        }
    }

    open fun onSaveInstanceStates(outState: Bundle) {
    }

    open fun onRestoreInstanceStates(savedInstanceState: Bundle) {
    }

    open fun onRestoreViewStates(savedInstanceState: Bundle) {
    }

    open fun onIntentExtras(extras: Bundle?, savedInstanceState: Bundle?) {
    }

    open fun createActivityTransaction(): OpenActivityTransaction {
        return activityOpener.createTransaction()
    }

    open fun createFragmentTransaction(): OpenFragmentTransaction {
        return fragmentDelegate.createTransaction()
    }

    abstract fun setupLayoutView(): Int

    open fun onPreCreate(savedInstanceState: Bundle?) {}

    open fun onBindView(savedInstanceState: Bundle?) {}

    open fun onDebuggingMode(savedInstanceState: Bundle?) {}

    open fun onInitialize(savedInstanceState: Bundle?) {}

    open fun onSetupView(savedInstanceState: Bundle?) {}

    open fun onPrepareInstanceState() {}

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
        fragmentDelegate.bindFragmentContainer(containerLayoutRes)
    }

    override
    fun initFragment(fragment: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
        fragmentDelegate.initFragment(fragment, savedInstanceState)
    }

    override
    fun initFragment(containerLayoutRes: Int, fragment: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
        fragmentDelegate.initFragment(containerLayoutRes, fragment, savedInstanceState)
    }

    override
    fun containFragment(tag: String): Boolean {
        return fragmentDelegate.containFragment(tag)
    }

    override
    fun clearBackStackFragmentTo(index: Int) {
        fragmentDelegate.clearBackStackFragmentTo(index)
    }

    override
    fun clearAllBackStacksFragment() {
        fragmentDelegate.clearAllBackStacksFragment()
    }

    override
    fun backPressedFragment() {
        fragmentDelegate.backPressedFragment()
    }

    override
    fun backPressedToFragment(tag: String) {
        fragmentDelegate.backPressedToFragment(tag)
    }

    override
    fun openFragmentSlideFromLeft(fragment: androidx.fragment.app.Fragment, withFinish: Boolean) {
        fragmentDelegate.openFragmentSlideFromLeft(fragment, withFinish)
    }

    override
    fun openFragmentSlideFromRight(fragment: androidx.fragment.app.Fragment, withFinish: Boolean) {
        fragmentDelegate.openFragmentSlideFromRight(fragment, withFinish)
    }

    override
    fun openFragment(fragment: androidx.fragment.app.Fragment, withFinish: Boolean) {
        fragmentDelegate.openFragment(fragment, withFinish)
    }

    private fun log(message: String) {
        if (enableLog && BuildConfig.DEBUG) {
            Timber.d(message)
        }
    }

}
