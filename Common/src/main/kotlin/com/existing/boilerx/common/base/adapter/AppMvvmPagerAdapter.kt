package com.existing.boilerx.common.base.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.existing.boilerx.core.base.mvvm.view.adapter.BaseMvvmPagerAdapter


/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

abstract class AppMvvmPagerAdapter
    : BaseMvvmPagerAdapter {



    constructor(activity: FragmentActivity) : super(activity) {
    }

    constructor(fragment: Fragment) : super(fragment) {}

}

