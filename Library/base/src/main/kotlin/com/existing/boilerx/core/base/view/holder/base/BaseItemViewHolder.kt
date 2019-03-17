package com.existing.boilerx.core.base.view.holder.base

import android.view.ViewGroup

/**
 * Created by「 The Khaeng 」on 23 Aug 2017 :)
 */

abstract class BaseItemViewHolder<I>(parent: ViewGroup, layout: Int)
    : BaseViewHolder(parent, layout) {

    var item: I? = null


    open fun onBind(item: I) {
        this.item = item
    }

}
