package com.existing.boilerx.core.base.mvvm.view.adapter


import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import com.existing.boilerx.core.base.view.holder.base.LoadmoreItem
import com.existing.boilerx.core.exception.NotCreateLoadMoreViewHolderException
import timber.log.Timber


/**
 * Created by「 The Khaeng 」on 20 Sep 2017 :)
 */

abstract class BaseLoadmoreListAdapter<VH : BaseViewHolder>
    : BaseListAdapter<VH>() {


    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    private val TAG: String = javaClass.simpleName

    val itemCountNoLoadmore: Int
        get() = if (isShowLoadmore) itemCount - 1 else itemCount


    var isLoadmore: Boolean = false


    private var isShowLoadmore: Boolean = false

    override
    fun getItemCount(): Int = if (isShowLoadmore) getItemCount(1) else getItemCount(0)

    abstract fun getItemCount(loadmoreCount: Int): Int


    @Deprecated("use getItemViewTypeWithLoadmore()")
    override
    fun getItemViewType(position: Int): Int {
        if (isShowLoadmore && position >= itemCount - 1) {
            return LoadmoreItem.TYPE
        }
        return getItemViewTypeWithLoadmore(position)
    }

    abstract fun getItemViewTypeWithLoadmore(position: Int): Int

    @Deprecated("use onCreateViewHolderWithLoadmore()")
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return if (isShowLoadmore && viewType == LoadmoreItem.TYPE) {
            onCreateLoadmoreViewHolder(parent)
        } else {
            onCreateViewHolderWithLoadmore(parent, viewType)
        }
    }

    open fun onCreateLoadmoreViewHolder(parent: ViewGroup): VH {
        throw NotCreateLoadMoreViewHolderException()
    }


    fun setOnLoadMoreListener(listener: OnLoadMoreListener?, visibleThreshold: Int = 1) {
        this.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("BinaryOperationInTimber")
            override
            fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = this@BaseLoadmoreListAdapter.recyclerView?.layoutManager
                if (dy > 0) { //scroll down
                    val totalItemCount = layoutManager?.itemCount ?: 0
                    var firstVisibleItemPosition = 0
                    val visibleItemCount = layoutManager?.childCount ?: 0
                    if (layoutManager is LinearLayoutManager) {
                        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        if (!isLoadmore
                            && (visibleItemCount + firstVisibleItemPosition + visibleThreshold) >= totalItemCount
                        ) {
                            listener?.onLoadMore()
                            isLoadmore = true
                        }
                    } else if (layoutManager is GridLayoutManager) {
                        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        if (!isLoadmore
                            && (visibleItemCount + firstVisibleItemPosition + visibleThreshold) >= totalItemCount
                        ) {
                            listener?.onLoadMore()
                            isLoadmore = true
                        }
                    } else if (layoutManager is StaggeredGridLayoutManager) {
                        var currentFirstVisible: IntArray? = null
                        currentFirstVisible = layoutManager.findFirstVisibleItemPositions(currentFirstVisible)
                        if (currentFirstVisible != null && currentFirstVisible.isNotEmpty()) {
                            firstVisibleItemPosition = currentFirstVisible[0]
                        }

                        if (!isLoadmore
                            && (visibleItemCount + firstVisibleItemPosition + visibleThreshold) >= totalItemCount
                        ) {
                            listener?.onLoadMore()
                            isLoadmore = true
                        }

                    } else {
                        Timber.w("Layout is not support loadmore: ")
                    }

                }

            }

        })
    }


    override
    fun onBindViewHolder(holder: VH, position: Int) {
        if (isShowLoadmore && position >= itemCount - 1) {
            return
        }
        onBindViewHolderWithLoadmore(holder, position)
    }


    abstract fun onCreateViewHolderWithLoadmore(parent: ViewGroup, viewType: Int): VH


    abstract fun onBindViewHolderWithLoadmore(holder: VH, position: Int)


    fun showLoadmore(show: Boolean) {
        isShowLoadmore = show
        if (show) {
            notifyItemInserted(itemCount)
        } else {
            isLoadmore = false
            notifyItemRemoved(itemCount)
        }
    }

}
