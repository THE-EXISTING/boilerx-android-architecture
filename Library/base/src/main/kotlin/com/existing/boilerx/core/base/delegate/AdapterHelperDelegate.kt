package com.existing.boilerx.core.base.delegate

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.existing.boilerx.core.base.mvvm.view.adapter.BaseDragAdapter
import com.existing.boilerx.core.base.mvvm.view.adapter.BaseLoadmoreListAdapter
import com.existing.boilerx.core.base.mvvm.view.adapter.OnItemListChangedListener
import com.existing.boilerx.core.base.utils.BaseItemListDiffCallback
import com.existing.boilerx.core.base.view.holder.base.BaseItem
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class AdapterHelperDelegate<VH : BaseViewHolder>(
    private var view: AdapterHelper<VH>
) : AdapterHelper<VH> {

    lateinit var adapter: Adapter<VH>

    override
    fun getItemCount(): Int = registerItemList().size

    override
    fun getItemViewType(position: Int): Int = registerItemList()[position].type

    override
    fun getItemId(position: Int): Long = registerItemList()[position].id

    override
    fun onCreateLoadmoreViewHolder(parent: ViewGroup): VH {
        return view.onCreateLoadmoreViewHolder(parent)
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return view.onCreateViewHolder(parent, viewType)
    }

    override
    fun onBindViewHolder(itemList: List<*>, holder: VH, position: Int) {
        view.onBindViewHolder(itemList, holder, position)
    }

    override
    fun registerItemList(): MutableList<BaseItem> = view.registerItemList()


    override
    fun setupAdapter(): AdapterHelperDelegate.Adapter<VH> = AdapterHelperDelegate.Adapter(this)

    override
    fun setOnItemListChangedListener(changePositionListener: OnItemListChangedListener) {
        adapter.setOnItemListChangedListener(changePositionListener)
    }

    override
    fun calculateDiffUtil(newItemList: List<BaseItem>?): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
            BaseItemListDiffCallback(
                registerItemList(),
                newItemList ?: mutableListOf()
            )
        )
    }

    override
    fun calculateDiffUtilAsync(newItemList: List<BaseItem>?): Single<DiffUtil.DiffResult> {
        return Single.just(Any())
            .subscribeOn(Schedulers.computation())
            .map {
                DiffUtil.calculateDiff(
                    BaseItemListDiffCallback(
                        registerItemList(),
                        newItemList ?: mutableListOf()
                    )
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override
    fun notifyDataSetChangedWithDiffUtil(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(adapter)
    }

    override
    fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    override
    fun notifyItemChanged(position: Int, payload: Any?) {
        adapter.notifyItemChanged(position, payload)
    }

    override
    fun notifyItemInserted(position: Int) {
        adapter.notifyItemInserted(position)
    }

    override
    fun notifyItemRemoved(position: Int) {
        adapter.notifyItemRemoved(position)
    }

    override
    fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(positionStart, itemCount, payload)
    }

    override
    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        adapter.notifyItemRangeRemoved(positionStart, itemCount)
    }

    override
    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        adapter.notifyItemRangeInserted(positionStart, itemCount)
    }

    override
    fun showLoadmore(show: Boolean) {
        adapter.showLoadmore(show)
    }

    override
    fun setOnLoadMoreListener(listener: BaseLoadmoreListAdapter.OnLoadMoreListener?, visibleThreshold: Int) {
        adapter.setOnLoadMoreListener(listener, visibleThreshold)
    }

    open class Adapter<VH : BaseViewHolder>(
        private val helper: AdapterHelper<VH>
    ) : BaseDragAdapter<VH>() {


        override
        fun getItemCount(loadmoreCount: Int): Int = loadmoreCount + helper.getItemCount()

        override
        fun getItemViewTypeWithLoadmore(position: Int): Int = helper.getItemViewType(position)

        override
        fun getItemId(position: Int): Long = helper.getItemId(position)

        override
        fun onCreateLoadmoreViewHolder(parent: ViewGroup): VH {
            return helper.onCreateLoadmoreViewHolder(parent)
        }

        override
        fun onCreateViewHolderWithLoadmore(parent: ViewGroup, viewType: Int): VH {
            return helper.onCreateViewHolder(parent, viewType)
        }

        override
        fun onBindViewHolderWithLoadmore(holder: VH, position: Int) {
            helper.onBindViewHolder(helper.registerItemList(), holder, position)
        }

        override
        fun onItemMove(fromPosition: Int, toPosition: Int) {
            if (changePositionListener != null) {
                Collections.swap(helper.registerItemList(), fromPosition, toPosition)
                changePositionListener?.onItemListChangedPosition(helper.registerItemList())
                notifyItemMoved(fromPosition, toPosition)
            }
        }


        override
        fun onItemDismiss(position: Int) {
        }


    }
}
