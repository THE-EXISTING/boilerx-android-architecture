package com.existing.boilerx.core.base.delegate

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.os.Bundle
import com.existing.boilerx.core.R
import com.existing.boilerx.ktx.notnull
import java.lang.ref.WeakReference


class FragmentHelperDelegate(
        fragmentActivity: androidx.fragment.app.FragmentActivity
)
    : FragmentHelper, LifecycleObserver {
    private val lifecycle: Lifecycle = fragmentActivity.lifecycle
    private var containerLayoutRes = -1
    private var fragmentActivity: WeakReference<androidx.fragment.app.FragmentActivity> = WeakReference(fragmentActivity)
    private var isCanCommit = false

    var openFragmentStack: Pair<androidx.fragment.app.Fragment, OpenFragmentTransaction>? = null

    private val view: androidx.fragment.app.FragmentActivity
        get() = fragmentActivity.get()!!


    private val visibleFragment: androidx.fragment.app.Fragment?
        get() {
            val fragmentManager = view.supportFragmentManager
            val fragments = fragmentManager.fragments
            fragments
                    .filter { it?.isVisible == true }
                    .forEach { return it }
            return null
        }


    override
    fun getCurrentFragment(): androidx.fragment.app.Fragment? = visibleFragment

    init {
        lifecycle.addObserver(this)
    }

    fun setCanCommit(canCommit: Boolean) {
        isCanCommit = canCommit
    }

    fun isCanCommit(fragment: androidx.fragment.app.Fragment, transaction: OpenFragmentTransaction): Boolean {
        val result = !isSameFragment(fragment) && isCanCommit
        if (!result) {
            openFragmentStack = Pair(fragment, transaction)
        }
        return result
    }


    override
    fun bindFragmentContainer(containerLayoutRes: Int) {
        this.containerLayoutRes = containerLayoutRes
    }

    override
    fun initFragment(fragment: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
        throwNullIfNotBind()
        if (savedInstanceState == null) {
            createTransaction()
                    .setContainerLayout(containerLayoutRes)
                    .add(view, fragment)
        }
    }

    override
    fun initFragment(containerLayoutRes: Int, fragment: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
        this.containerLayoutRes = containerLayoutRes
        if (savedInstanceState == null) {
            createTransaction()
                    .setContainerLayout(containerLayoutRes)
                    .add(view, fragment)
        }
    }


    override
    fun containFragment(tag: String): Boolean {
        if (view.supportFragmentManager.fragments.size == 0) {
            return false
        }
        return view.supportFragmentManager.fragments.any { it?.tag?.contains(tag) == true }
    }

    override
    fun clearBackStackFragmentTo(index: Int) {
        if (view.supportFragmentManager.backStackEntryCount > 0) {
            val count = view.supportFragmentManager.backStackEntryCount
            for (i in 0 until count) {
                view.supportFragmentManager.popBackStackImmediate()
                if (i == count - index) break
            }
        }
    }

    override
    fun clearAllBackStacksFragment() {
        if (view.supportFragmentManager.backStackEntryCount > 0) {
            clearBackStackFragmentTo(0)
        }

    }

    override
    fun backPressedFragment() {
        view.supportFragmentManager.popBackStack()
    }

    override
    fun backPressedToFragment(tag: String) {
        if (containFragment(tag)) {
            view.supportFragmentManager.popBackStackImmediate(tag, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override
    fun openFragmentSlideFromLeft(fragment: androidx.fragment.app.Fragment, withFinish: Boolean) {
        val transaction = createTransaction()
                .setFinish(withFinish)
                .setContainerLayout(containerLayoutRes)
                .setOpenAnim(R.anim.fragment_action_pop_slide_another_from_left_in, R.anim.fragment_action_pop_slide_current_to_right_out)
                .setPopAnim(R.anim.fragment_action_open_slide_another_from_right_in, R.anim.fragment_action_open_slide_current_to_left_out)
                .addToBackStack(fragment.javaClass.simpleName)

        if (!isCanCommit(fragment, transaction)) return
        transaction.open(view, fragment)
    }


    override
    fun openFragmentSlideFromRight(fragment: androidx.fragment.app.Fragment, withFinish: Boolean) {
        val transaction = createTransaction()
                .setFinish(withFinish)
                .setContainerLayout(containerLayoutRes)
                .setOpenAnim(R.anim.fragment_action_open_slide_another_from_right_in, R.anim.fragment_action_open_slide_current_to_left_out)
                .setPopAnim(R.anim.fragment_action_pop_slide_another_from_left_in, R.anim.fragment_action_pop_slide_current_to_right_out)
                .addToBackStack(fragment.javaClass.simpleName)
        if (!isCanCommit(fragment, transaction)) return
        transaction.open(view, fragment)
    }

    override
    fun openFragment(fragment: androidx.fragment.app.Fragment, withFinish: Boolean) {
        val transaction = createTransaction()
                .setFinish(withFinish)
                .setContainerLayout(containerLayoutRes)
                .addToBackStack(fragment.javaClass.simpleName)
        if (!isCanCommit(fragment, transaction)) return
        transaction.open(view, fragment)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyView() {
        lifecycle.removeObserver(this)
    }

    fun handleBackButton() {
        if (view.supportFragmentManager.backStackEntryCount > 1) {
            view.supportFragmentManager.popBackStack()
        } else {
            view.finish()
        }
    }

    fun openFragmentInStackIfAvailable() {
        openFragmentStack?.notnull { fragment, transaction ->
            transaction.open(view, fragment)
            openFragmentStack = null
        }
    }

    fun createTransaction(): OpenFragmentTransaction {
        return OpenFragmentTransaction()
    }
    /* ============================ Private method ============================================== */

    private fun isSameFragment(fragment: androidx.fragment.app.Fragment): Boolean {
        val currentFragment = getCurrentFragment()
        return null != currentFragment && currentFragment.javaClass == fragment.javaClass
    }

    private fun throwNullIfNotBind() {
        if (containerLayoutRes == -1)
            throw NullPointerException("bindFragmentContainer() first.")
    }


}
