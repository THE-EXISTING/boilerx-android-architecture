package com.existing.boilerx.common.base.mvvm


import com.existing.boilerx.common.base.mvvm.error.handling.ErrorHandlingMvvmRecyclerViewActivity
import com.existing.boilerx.common.R
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_recycler_view.activity_recycler_toolbar as toolbar
import kotlinx.android.synthetic.main.activity_recycler_view.activity_recycler_view as activityRecyclerView

/**
 * Created by「 The Khaeng 」on 25 Aug 2017 :)
 */

@Suppress("SortModifiers")
abstract class AppMvvmRecyclerViewActivity<VH : BaseViewHolder>
    : ErrorHandlingMvvmRecyclerViewActivity<VH>() {


    override
    fun setupLayoutView(): Int = R.layout.activity_recycler_view



}

