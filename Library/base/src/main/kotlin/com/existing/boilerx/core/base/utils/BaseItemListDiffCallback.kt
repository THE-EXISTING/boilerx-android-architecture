package com.existing.boilerx.core.base.utils


import androidx.recyclerview.widget.DiffUtil
import com.existing.boilerx.core.base.view.holder.base.BaseItem

/**
 * Created by「 The Khaeng 」on 28 Mar 2018 :)
 */
class BaseItemListDiffCallback(
    private val oldOrderItemList: List<BaseItem>,
    private val newOrderItemList: List<BaseItem>
) : DiffUtil.Callback() {

    override
    fun getOldListSize(): Int = oldOrderItemList.size

    override
    fun getNewListSize(): Int = newOrderItemList.size

    override
    fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldOrderItemList[oldItemPosition].id == newOrderItemList[newItemPosition].id

    override
    fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldOrderItemList[oldItemPosition] == newOrderItemList[newItemPosition]
}
