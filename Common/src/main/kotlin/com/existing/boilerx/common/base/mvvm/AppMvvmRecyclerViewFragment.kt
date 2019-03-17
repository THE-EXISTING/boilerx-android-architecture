package com.existing.boilerx.common.base.mvvm

import com.existing.boilerx.common.base.mvvm.error.handling.ErrorHandlingMvvmRecyclerViewFragment
import com.existing.boilerx.common.R
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder


/**
 * Created by「 The Khaeng 」on 25 Aug 2017 :)
 */

@Suppress("SortModifiers")
abstract class AppMvvmRecyclerViewFragment<VH : BaseViewHolder>
    : ErrorHandlingMvvmRecyclerViewFragment<VH>() {


    override
    fun setupLayoutView(): Int = R.layout.fragment_recycler_view


}

