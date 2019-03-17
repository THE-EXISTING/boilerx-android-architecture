package com.existing.boilerx.core.base.mvvm.view.adapter


import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder


/**
 * Created by「 The Khaeng 」on 19 Sep 2017 :)
 */

@Suppress("SortModifiers")
abstract class BaseDragAdapter<
        VH : BaseViewHolder>
    : BaseLoadmoreListAdapter<VH>(), ItemTouchHelperAdapter {

    protected var changePositionListener: OnItemListChangedListener? = null
    protected var dragListener: OnStartDragListener? = null

    fun setOnItemListChangedListener(changePositionListener: OnItemListChangedListener){
        this.changePositionListener = changePositionListener
    }

    fun setOnStartDragListener(dragListener: OnStartDragListener){
        this.dragListener = dragListener
    }


}

