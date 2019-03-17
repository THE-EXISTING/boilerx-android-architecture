package com.existing.boilerx.core.base.mvvm.view.adapter

/**
 * Created by「 The Khaeng 」on 06 Sep 2018 :)
 */
interface ItemTouchHelperViewHolder {
    /**
     * Implementations should update the item view to indicate it's active state.
     */
    fun onItemSelected()


    /**
     * state should be cleared.
     */
    fun onItemClear()
}