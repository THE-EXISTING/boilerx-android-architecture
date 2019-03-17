package com.existing.boilerx.core.base.delegate

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.existing.boilerx.core.base.mvvm.view.adapter.BaseLoadmoreListAdapter
import com.existing.boilerx.core.base.mvvm.view.adapter.OnItemListChangedListener
import com.existing.boilerx.core.base.view.holder.base.BaseItem
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import io.reactivex.Single

/**
 * Created by「 The Khaeng 」on 31 Aug 2017 :)
 */

interface AdapterHelper<VH : BaseViewHolder> {

    fun getItemCount(): Int

    fun getItemViewType(position: Int): Int

    fun getItemId(position: Int): Long

    fun onCreateLoadmoreViewHolder(parent: ViewGroup): VH

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    fun onBindViewHolder(itemList: List<*>, holder: VH, position: Int)

    fun registerItemList(): MutableList<BaseItem>


    fun setupAdapter(): AdapterHelperDelegate.Adapter<VH>

    fun setOnItemListChangedListener(changePositionListener: OnItemListChangedListener)

    fun calculateDiffUtil(newItemList: List<BaseItem>?): DiffUtil.DiffResult

    fun calculateDiffUtilAsync(newItemList: List<BaseItem>?): Single<DiffUtil.DiffResult>

    fun notifyDataSetChangedWithDiffUtil(result: DiffUtil.DiffResult)

    fun notifyDataSetChanged()

    fun notifyItemChanged(position: Int, payload: Any? = null)

    fun notifyItemInserted(position: Int)

    fun notifyItemRemoved(position: Int)

    fun notifyItemMoved(fromPosition: Int, toPosition: Int)

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any? = null)

    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int)

    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int)

    fun showLoadmore(show: Boolean)

    fun setOnLoadMoreListener(listener: BaseLoadmoreListAdapter.OnLoadMoreListener?, visibleThreshold: Int = 1)


}
