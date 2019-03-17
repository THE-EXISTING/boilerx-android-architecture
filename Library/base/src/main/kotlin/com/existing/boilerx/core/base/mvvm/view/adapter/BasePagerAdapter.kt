package com.existing.boilerx.core.base.mvvm.view.adapter

import android.os.Build
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.existing.boilerx.core.base.mvvm.view.BaseFragment
import java.lang.ref.WeakReference

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

abstract class BasePagerAdapter(
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    var fragmentList: Array<Fragment?> = arrayOfNulls(0)
    private var fm: WeakReference<FragmentManager> = WeakReference(fragmentManager)

    val fragmentManager: FragmentManager
        get() = fm.get()!!


    private var windowInsets: WindowInsets? = null


    override
    fun instantiateItem(container: ViewGroup, position: Int): Any {
        updateFragmentList()
        val fragment = super.instantiateItem(container, position) as Fragment
        fragmentList[position] = fragment
        return fragment.apply {
            if (this is BaseFragment) {
                windowInsets?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        this.setWindowsInsets(WindowInsets(it))
                    }
                }
            }
        }
    }


    @Deprecated("use BasePagerAdapter.getItem(position, fragmentList)",
                ReplaceWith("getItem(position: Int, fragmentList: Array<Fragment?>)"))
    override
    fun getItem(position: Int): Fragment {
        updateFragmentList()
        fragmentList[position] = getFragmentItem(position)
        return fragmentList[position]!!
    }

    abstract fun getFragmentItem(position: Int): Fragment?


    fun setWindowInsets(insets: WindowInsets?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            windowInsets = WindowInsets(insets)
        }
    }


    private fun updateFragmentList() {
        if (fragmentList.size != count) {
            val tmpFragmentList = arrayOfNulls<Fragment?>(count)
            tmpFragmentList.forEachIndexed { index, fragment ->
                if (index < fragmentList.size) {
                    tmpFragmentList[index] = fragmentList[index]
                }
            }
            fragmentList = tmpFragmentList
        }
    }

}
