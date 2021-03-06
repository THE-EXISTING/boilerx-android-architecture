package com.existing.boilerx.core.base.delegate

import android.annotation.SuppressLint
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes

/**
 * Created by「 The Khaeng 」on 08 Oct 2017 :)
 */

class OpenFragmentTransaction(
        private var isFinish: Boolean = false,
        private var backStack: String = "",
        private var enter: Int = NO_ASSIGN,
        private var exit: Int = NO_ASSIGN,
        private var popEnter: Int = NO_ASSIGN,
        private var popExit: Int = NO_ASSIGN,
        private var containerLayoutRes: Int = NO_ASSIGN) {

    companion object {
        private const val NO_ASSIGN = 0
    }

    fun setFinish(isFinish: Boolean): OpenFragmentTransaction {
        this.isFinish = isFinish
        return this
    }

    fun addToBackStack(backStack: String): OpenFragmentTransaction {
        this.backStack = backStack
        return this
    }

    fun setOpenAnim(@AnimRes enter: Int, @AnimRes exit: Int): OpenFragmentTransaction {
        this.enter = enter
        this.exit = exit
        return this
    }

    fun setPopAnim(@AnimRes popEnter: Int, @AnimRes popExit: Int): OpenFragmentTransaction {
        this.popEnter = popEnter
        this.popExit = popExit
        return this

    }

    fun setContainerLayout(@LayoutRes layout: Int): OpenFragmentTransaction {
        this.containerLayoutRes = layout
        return this
    }

    @SuppressLint("CommitTransaction")
    fun open(activity: androidx.fragment.app.FragmentActivity, fragment: androidx.fragment.app.Fragment) {

        if (containerLayoutRes == NO_ASSIGN)
            throw RuntimeException("setContainerLayout(layout) before open.")

        val tag = fragment.javaClass.simpleName
        val transaction = getDefaultFragmentTransactionWithAnim(activity)
                .replace(containerLayoutRes, fragment, tag)
                .addToBackStack(tag)
        if (isFinish) {
            activity.supportFragmentManager.popBackStackImmediate()
        }

        transaction.commit()
    }

    @SuppressLint("CommitTransaction")
    fun add(activity: androidx.fragment.app.FragmentActivity, fragment: androidx.fragment.app.Fragment) {
        if (containerLayoutRes == NO_ASSIGN)
            throw RuntimeException("setContainerLayout(layout) before open.")
        val tag = fragment.javaClass.simpleName
        val transaction = getDefaultFragmentTransaction(activity)
                .add(containerLayoutRes, fragment, tag)
        if (backStack.isNotEmpty()) transaction.addToBackStack(tag)
        transaction.commit()
    }

    /* =========================== Private method ============================================= **/
    @SuppressLint("CommitTransaction")
    private fun getDefaultFragmentTransaction(activity: androidx.fragment.app.FragmentActivity): androidx.fragment.app.FragmentTransaction {
        return activity.supportFragmentManager
                .beginTransaction()
    }

    private fun getDefaultFragmentTransactionWithAnim(activity: androidx.fragment.app.FragmentActivity): androidx.fragment.app.FragmentTransaction {
        val transaction = getDefaultFragmentTransaction(activity)
        if ((enter != NO_ASSIGN && exit != NO_ASSIGN)
                || (popEnter != NO_ASSIGN && popExit != NO_ASSIGN)) {
            transaction.setCustomAnimations(
                    enter, exit,
                    popEnter, popExit)
        }
        return transaction
    }


}
