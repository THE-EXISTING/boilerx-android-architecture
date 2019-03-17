package com.existing.boilerx.core.base.view.holder.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.existing.boilerx.core.R
import com.existing.boilerx.core.base.delegate.AdapterHelper
import com.existing.boilerx.core.base.delegate.AdapterHelperDelegate
import com.existing.boilerx.core.base.mvvm.view.BaseMvvmRecyclerViewFragment
import com.existing.boilerx.core.base.mvvm.view.adapter.BaseLoadmoreListAdapter
import com.existing.boilerx.core.base.mvvm.view.adapter.OnItemListChangedListener
import com.existing.boilerx.core.base.utils.DefaultLinearLayoutManager
import com.existing.boilerx.core.exception.NotCreateLoadMoreViewHolderException
import com.existing.boilerx.core.exception.TypeNotMatchInAdapterException
import io.reactivex.Single
import kotlinx.android.synthetic.main.holder_recycler_view.view.holder_nested_recycler_view as nestRecyclerView

/**
 * Created by「 The Khaeng 」on 23 Aug 2017 :)
 */

abstract class BaseRecyclerViewHolder<I : BaseItem, VH : BaseViewHolder>(
    parent: ViewGroup,
    layout: Int = R.layout.holder_recycler_view
) : BaseItemViewHolder<I>(parent, layout), AdapterHelper<VH> {

    companion object {
        val TAG: String = BaseItemViewHolder::class.java.simpleName
    }

    var itemList: MutableList<BaseItem> = mutableListOf()

    private val delegate: AdapterHelperDelegate<VH> = AdapterHelperDelegate(this)
    val adapter: AdapterHelperDelegate.Adapter<VH> get() = delegate.adapter

    init {
        delegate.adapter = setupAdapter()
        val recyclerView = setupRecyclerView()
        onSetupRecyclerView(recyclerView ?: itemView.nestRecyclerView, delegate.adapter)
        (recyclerView ?: itemView.nestRecyclerView)?.apply {
            layoutManager = setupLayoutManager()
            adapter = delegate.adapter
        }
    }

    open fun setupRecyclerView(): RecyclerView? = null

    open fun setupLayoutManager(): RecyclerView.LayoutManager =
        DefaultLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    override
    fun setupAdapter(): AdapterHelperDelegate.Adapter<VH> = delegate.setupAdapter()

    open fun onSetupRecyclerView(recyclerView: RecyclerView?, adapter: AdapterHelperDelegate.Adapter<VH>) {

    }

    fun setupItemList(itemList: MutableList<BaseItem>) {
        this.itemList = itemList
    }

    override
    fun registerItemList(): MutableList<BaseItem> = itemList


    override
    fun getItemCount(): Int {
        return registerItemList().size
    }

    override
    fun getItemViewType(position: Int): Int {
        return registerItemList()[position].type
    }

    override
    fun getItemId(position: Int): Long {
        return registerItemList()[position].id
    }

    override
    fun onCreateLoadmoreViewHolder(parent: ViewGroup): VH {
        throw NotCreateLoadMoreViewHolderException()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        throw TypeNotMatchInAdapterException(BaseMvvmRecyclerViewFragment.TAG, viewType)
    }


    override
    fun onBindViewHolder(itemList: List<*>, holder: VH, position: Int) {
    }

    override
    fun calculateDiffUtil(newItemList: List<BaseItem>?): DiffUtil.DiffResult {
        return delegate.calculateDiffUtil(newItemList)
    }

    override
    fun calculateDiffUtilAsync(newItemList: List<BaseItem>?): Single<DiffUtil.DiffResult> {
        return delegate.calculateDiffUtilAsync(newItemList)
    }


    override
    fun notifyDataSetChangedWithDiffUtil(result: DiffUtil.DiffResult) {
        delegate.notifyDataSetChangedWithDiffUtil(result)
    }

    override
    fun setOnItemListChangedListener(changePositionListener: OnItemListChangedListener) {
        delegate.setOnItemListChangedListener(changePositionListener)
    }

    override
    fun notifyDataSetChanged() {
        delegate.notifyDataSetChanged()
    }

    override
    fun notifyItemChanged(position: Int, payload: Any?) {
        delegate.notifyItemChanged(position, payload)
    }

    override
    fun notifyItemInserted(position: Int) {
        delegate.notifyItemInserted(position)
    }

    override
    fun notifyItemRemoved(position: Int) {
        delegate.notifyItemRemoved(position)
    }

    override
    fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        delegate.notifyItemMoved(fromPosition, toPosition)
    }

    override
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        delegate.notifyItemRangeChanged(positionStart, itemCount, payload)
    }

    override
    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        delegate.notifyItemRangeRemoved(positionStart, itemCount)
    }

    override
    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        delegate.notifyItemRangeInserted(positionStart, itemCount)
    }

    override
    fun showLoadmore(show: Boolean) {
        delegate.showLoadmore(show)
    }

    override
    fun setOnLoadMoreListener(listener: BaseLoadmoreListAdapter.OnLoadMoreListener?, visibleThreshold: Int) {
        delegate.setOnLoadMoreListener(listener, visibleThreshold)
    }
}
