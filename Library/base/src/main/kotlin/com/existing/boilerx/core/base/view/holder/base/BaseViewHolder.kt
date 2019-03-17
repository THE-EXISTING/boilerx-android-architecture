package com.existing.boilerx.core.base.view.holder.base

import android.content.Context
import android.text.Editable
import android.text.NoCopySpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by「 The Khaeng 」on 23 Aug 2017 :)
 */

abstract class BaseViewHolder(parent: ViewGroup, layout: Int)
    : RecyclerView.ViewHolder(LayoutInflater
                                      .from(parent.context)
                                      .inflate(layout, parent, false)) {


    val recyclerView: RecyclerView = parent as RecyclerView

    val context: Context get() = recyclerView.context

    val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager

    interface OnClickListener {
        fun onClick(view: View?, position: Int)
    }

    interface OnLongClickListener {
        fun onLongClick(view: View?, position: Int): Boolean
    }

    abstract class TextWatcher : NoCopySpan {

        open fun afterTextChanged(editable: Editable?, position: Int){}

        open fun beforeTextChanged(s: CharSequence?, start: Int,
                                   count: Int, after: Int, position: Int){}

        open fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int, position: Int){}

    }

    open var clickListener: BaseViewHolder.OnClickListener? = null
        set(value) {
            field = value
            itemView.rootView.setOnClickListener { v ->
                clickListener?.onClick(v, adapterPosition)
            }
        }

    open var longClickListener: BaseViewHolder.OnLongClickListener? = null
        set(value) {
            field = value
            itemView.rootView.setOnLongClickListener { v ->
                longClickListener?.onLongClick(v, adapterPosition)
                true
            }
        }


    init {
        onBindView(itemView)
    }

    open fun onBindView(view: View) {}



    fun getDimension(@DimenRes resId: Int): Float {
        return itemView.context.resources.getDimension(resId)
    }

    fun getString(@StringRes resId: Int): String {
        return itemView.context.resources.getString(resId)
    }


}
