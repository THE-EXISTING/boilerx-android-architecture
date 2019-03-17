package com.existing.boilerx.core.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

/**
 * Created by「 The Khaeng 」on 25 Apr 2018 :)
 */
class NestedWebView : WebView, NestedScrollingChild {
    private var lastY: Int = 0
    private val scrollOffset = IntArray(2)
    private val scrollConsumed = IntArray(2)
    private var nestedOffsetY: Int = 0
    private val childHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        isNestedScrollingEnabled = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override
    fun onTouchEvent(ev: MotionEvent): Boolean {
        var returnValue = false

        val event = MotionEvent.obtain(ev)
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            nestedOffsetY = 0
        }
        val eventY = event.y.toInt()
        event.offsetLocation(0f, nestedOffsetY.toFloat())
        when (action) {
            MotionEvent.ACTION_MOVE -> {
                var deltaY = lastY - eventY
                // NestedPreScroll
                if (dispatchNestedPreScroll(0, deltaY, scrollConsumed, scrollOffset)) {
                    deltaY -= scrollConsumed[1]
                    lastY = eventY - scrollOffset[1]
                    event.offsetLocation(0f, (-scrollOffset[1]).toFloat())
                    nestedOffsetY += scrollOffset[1]
                }
                returnValue = super.onTouchEvent(event)

                // NestedScroll
                if (dispatchNestedScroll(0, scrollOffset[1], 0, deltaY, scrollOffset)) {
                    event.offsetLocation(0f, scrollOffset[1].toFloat())
                    nestedOffsetY += scrollOffset[1]
                    lastY -= scrollOffset[1]
                }
            }
            MotionEvent.ACTION_DOWN -> {
                returnValue = super.onTouchEvent(event)
                lastY = eventY
                // start NestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                returnValue = super.onTouchEvent(event)
                // end NestedScroll
                stopNestedScroll()
            }
        }
        return returnValue
    }

    // Nested Scroll implements
    override
    fun setNestedScrollingEnabled(enabled: Boolean) {
        childHelper.isNestedScrollingEnabled = enabled
    }

    override
    fun isNestedScrollingEnabled(): Boolean {
        return childHelper.isNestedScrollingEnabled
    }

    override
    fun startNestedScroll(axes: Int): Boolean {
        return childHelper.startNestedScroll(axes)
    }

    override
    fun stopNestedScroll() {
        childHelper.stopNestedScroll()
    }

    override
    fun hasNestedScrollingParent(): Boolean {
        return childHelper.hasNestedScrollingParent()
    }

    override
    fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
                             offsetInWindow: IntArray?): Boolean {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override
    fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override
    fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override
    fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

}
