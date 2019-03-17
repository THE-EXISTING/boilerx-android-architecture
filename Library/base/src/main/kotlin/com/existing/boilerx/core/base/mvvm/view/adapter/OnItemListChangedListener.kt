package com.existing.boilerx.core.base.mvvm.view.adapter

import com.existing.boilerx.core.base.view.holder.base.BaseItem


/**
 * Created by「 The Khaeng 」on 06 Sep 2018 :)
 */
interface OnItemListChangedListener {
    fun onItemListChangedPosition(itemList: List<BaseItem>)
}
