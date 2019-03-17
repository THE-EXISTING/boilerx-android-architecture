package com.existing.boilerx.common.base.adapter


import com.existing.boilerx.core.base.mvvm.view.adapter.BaseListAdapter
import com.existing.boilerx.core.base.view.holder.base.BaseItemViewHolder


/**
 * Created by「 The Khaeng 」on 19 Sep 2017 :)
 */

abstract class AppAdapter<
        VH : BaseItemViewHolder<*>> : BaseListAdapter<VH>() {

}

