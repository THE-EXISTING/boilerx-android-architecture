package com.existing.boilerx.core.base.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by「 The Khaeng 」on 23 Aug 2017 :)
 */

open class DefaultLinearLayoutManager : LinearLayoutManager {

    private var context: Context? = null

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }


    /**
     * Set for preload in RecyclerView.
     *
     * @param state
     */
    override
    fun getExtraLayoutSpace(state: androidx.recyclerview.widget.RecyclerView.State): Int {
        return 300
    }


    private fun init(context: Context?) {
        this.context = context
    }

    override
    fun onLayoutChildren(recycler: androidx.recyclerview.widget.RecyclerView.Recycler?, state: androidx.recyclerview.widget.RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        }catch (e: IllegalStateException){
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }



}
