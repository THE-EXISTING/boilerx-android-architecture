package com.existing.boilerx.core.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.Checkable
import android.widget.LinearLayout


/**
 * Created by「 The Khaeng 」on 18 Jun 2018 :)
 */
class CheckableLayout : LinearLayout {

    private var listener: OnCheckedChangeListener? = null

    constructor(context: Context) : super(context, null) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}


    interface OnCheckedChangeListener {
        fun onCheckedChanged(v: View, isChecked: Boolean)
    }

    override
    fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        child?.setOnClickListener(onClickListener())
    }


    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        this.listener = listener
    }

    private fun onClickListener(): OnClickListener =
            OnClickListener { v ->
                if (v is Checkable) {
                    for (i in 0 until childCount) {
                        val child = getChildAt(i)
                        if (child is Checkable) {
                            (child as Checkable).isChecked = false
                        }
                    }
                    v.toggle()
                    listener?.onCheckedChanged(v, v.isChecked)
                }
            }

    fun clearChecked() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is Checkable) {
                (child as Checkable).isChecked = false
            }
        }
    }


}