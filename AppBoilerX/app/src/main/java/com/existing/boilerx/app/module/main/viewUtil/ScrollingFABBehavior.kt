package com.existing.boilerx.app.module.main.viewUtil

import android.content.Context
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View


/**
 * Created by「 The Khaeng 」on 21 Oct 2017 :)
 */

class ScrollingFABBehavior(context: Context, attrs: AttributeSet)
    : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {
    private val toolbarHeight: Int

    init {
        toolbarHeight = getToolbarHeight(context)
    }

    override
    fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override
    fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, fab: FloatingActionButton, dependency: View): Boolean {
        if (dependency is AppBarLayout) {
            val lp = fab.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            val fabBottomMargin = lp.bottomMargin
            val distanceToScroll = fab.height + fabBottomMargin
            val ratio = dependency.y / toolbarHeight.toFloat()
            fab.translationY = (-distanceToScroll * ratio) + (distanceToScroll / 2)
        }
        return true
    }

    private fun getToolbarHeight(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else {
            0
        }
    }
}
