package com.existing.boilerx.common.base.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.existing.boilerx.core.base.mvvm.view.BaseMvvmLoadmoreListAdapter
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder


/**
 * Created by「 The Khaeng 」on 19 Sep 2017 :)
 */

@Suppress("SortModifiers")
abstract class AppMvvmAdapter<
        VH : BaseViewHolder>
    : BaseMvvmLoadmoreListAdapter<VH> {


    constructor(activity: FragmentActivity) : super(activity) {}

    constructor(fragment: Fragment) : super(fragment) {}


}

