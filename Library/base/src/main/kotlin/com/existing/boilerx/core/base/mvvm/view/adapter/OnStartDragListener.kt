package com.existing.boilerx.core.base.mvvm.view.adapter

import androidx.recyclerview.widget.RecyclerView


/**
 * Created by「 The Khaeng 」on 06 Sep 2018 :)
 */
interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}
