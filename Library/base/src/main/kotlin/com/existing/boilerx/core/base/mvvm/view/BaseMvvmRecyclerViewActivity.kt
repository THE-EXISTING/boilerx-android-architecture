package com.existing.boilerx.core.base.mvvm.view


import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.existing.boilerx.core.base.delegate.AdapterHelper
import com.existing.boilerx.core.base.delegate.AdapterHelperDelegate
import com.existing.boilerx.core.base.mvvm.view.adapter.BaseLoadmoreListAdapter
import com.existing.boilerx.core.base.mvvm.view.adapter.OnItemListChangedListener
import com.existing.boilerx.core.base.utils.DefaultLinearLayoutManager
import com.existing.boilerx.core.base.view.holder.base.BaseItem
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import com.existing.boilerx.core.exception.NotCreateLoadMoreViewHolderException
import com.existing.boilerx.core.exception.TypeNotMatchInAdapterException
import io.reactivex.Single

/**
 * Created by「 The Khaeng 」on 25 Aug 2017 :)
 */

@Suppress("SortModifiers")
abstract class BaseMvvmRecyclerViewActivity<VH : BaseViewHolder>
    : BaseMvvmActivity(), AdapterHelper<VH> {

    companion object {
        val TAG: String = BaseMvvmRecyclerViewActivity::class.java.simpleName
    }

    var recyclerView: RecyclerView? = null
    val adapter: AdapterHelperDelegate.Adapter<VH> get() = delegate.adapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var delegate: AdapterHelperDelegate<VH>

    override
    fun onSetupView(savedInstanceState: Bundle?) {
        super.onSetupView(savedInstanceState)
        delegate = AdapterHelperDelegate(this)
        layoutManager = onSetupLayoutManager()
        delegate.adapter = setupAdapter()
        recyclerView = setupRecyclerView()?.apply {
            this.layoutManager = this@BaseMvvmRecyclerViewActivity.layoutManager
            this.adapter = delegate.adapter
        }
        onSetupRecyclerView(recyclerView, delegate.adapter)
    }

    abstract fun setupRecyclerView(): RecyclerView?

    open fun onSetupLayoutManager(): RecyclerView.LayoutManager = DefaultLinearLayoutManager(this)

    override
    fun setupAdapter(): AdapterHelperDelegate.Adapter<VH> = delegate.setupAdapter()

    open fun onSetupRecyclerView(recyclerView: RecyclerView?, adapter: AdapterHelperDelegate.Adapter<VH>) {
    }

    override
    abstract fun registerItemList(): MutableList<BaseItem>

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
        throw TypeNotMatchInAdapterException(TAG, viewType)
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

