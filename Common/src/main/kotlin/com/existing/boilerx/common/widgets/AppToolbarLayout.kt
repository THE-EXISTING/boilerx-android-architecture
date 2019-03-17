package com.existing.boilerx.common.widgets

import android.content.Context
import androidx.appcompat.widget.Toolbar
import android.util.AttributeSet


/**
 * Created by「 The Khaeng 」on 14 Feb 2018 :)
 */

class AppToolbarLayout
@JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle)
    : Toolbar(context, attrs, defStyleAttr) {

    init {
        setup(context)
    }

    fun setup(context: Context) {
        setupView()
    }


    fun setupView() {
        setInset()
    }

    private fun setInset() {
        setContentInsetsAbsolute(0, 0)
        setContentInsetsRelative(0, 0)
        setPadding(0, 0, 0, 0) //require: for tablet
    }
}
