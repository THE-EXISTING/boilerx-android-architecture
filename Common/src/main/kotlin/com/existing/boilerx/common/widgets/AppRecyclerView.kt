package com.existing.boilerx.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.existing.boilerx.common.R
import com.existing.boilerx.ktx.autoAnimate
import com.existing.boilerx.ktx.bouncedAction
import com.existing.boilerx.ktx.view.addPaddingBottom
import com.existing.boilerx.ktx.view.hide
import com.existing.boilerx.ktx.view.show

@Suppress("UNUSED_PARAMETER")
class AppRecyclerView
@JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr) {


    init {
        init(context, attrs)
        initWithAttrs(attrs, defStyleAttr, 0)
    }


    private fun init(context: Context, attrs: AttributeSet?) {}

    private fun initWithAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {}

    fun enablePerformanceMode() {
        setHasFixedSize(true)
        setItemViewCacheSize(5)
    }



    fun registerScrollTopButton(container: ViewGroup, view: View) {
        view.hide()
        addPaddingBottom(context.resources.getDimension(R.dimen.recycler_view_scroll_to_top_margin).toInt())
        view.setOnClickListener { smoothScrollToPosition(0) }
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override
            fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateScrollToTopView(container, view)
            }
        })
    }


    private fun updateScrollToTopView(container: ViewGroup, view: View) {
        val layout = layoutManager
        if (layout is LinearLayoutManager) {
            if (layout.findFirstCompletelyVisibleItemPosition() == 0) {
                container.autoAnimate()
                view.hide()
            } else {
                bouncedAction("scroll_to_top:fade_in", 50) {
                    container.autoAnimate()
                    view.show()
                }
            }
        }
    }

    override
    fun canScrollVertically(direction: Int): Boolean {
        // check if scrolling up
        if (direction < 1) {
            val original = super.canScrollVertically(direction)
            return !original && getChildAt(0) != null && getChildAt(0).top < 0 || original
        }
        return super.canScrollVertically(direction)

    }


}