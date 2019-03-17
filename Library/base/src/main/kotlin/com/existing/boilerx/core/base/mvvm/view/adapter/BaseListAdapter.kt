package com.existing.boilerx.core.base.mvvm.view.adapter


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder


/**
 * Created by「 The Khaeng 」on 20 Sep 2017 :)
 */

@Suppress("PrivatePropertyName")
abstract class BaseListAdapter<VH : BaseViewHolder>()
    : RecyclerView.Adapter<VH>() {

    var context: Context? = null
    private val TAG: String = javaClass.simpleName

    var recyclerView: RecyclerView? = null
        private set


    override
    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.context = recyclerView.context
    }


}
