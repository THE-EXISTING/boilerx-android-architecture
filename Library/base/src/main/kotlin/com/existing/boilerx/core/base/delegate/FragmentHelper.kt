package com.existing.boilerx.core.base.delegate

import android.os.Bundle

/**
* Created by「 The Khaeng 」on 08 Oct 2017 :)
*/

interface FragmentHelper {

    fun getCurrentFragment(): androidx.fragment.app.Fragment?

    fun bindFragmentContainer(containerLayoutRes: Int)

    fun initFragment(fragment: androidx.fragment.app.Fragment, savedInstanceState: Bundle?)

    fun initFragment(containerLayoutRes: Int, fragment: androidx.fragment.app.Fragment, savedInstanceState: Bundle?)

    fun containFragment(tag: String): Boolean

    fun clearBackStackFragmentTo(index: Int)

    fun clearAllBackStacksFragment()

    fun backPressedFragment()

    fun backPressedToFragment(tag: String)

    fun openFragmentSlideFromLeft(fragment: androidx.fragment.app.Fragment, withFinish: Boolean = false)

    fun openFragmentSlideFromRight(fragment: androidx.fragment.app.Fragment, withFinish: Boolean = false)

    fun openFragment(fragment: androidx.fragment.app.Fragment, withFinish: Boolean = false)
}
