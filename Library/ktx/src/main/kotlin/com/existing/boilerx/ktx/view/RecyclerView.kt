package com.existing.boilerx.ktx.view

/**
 * Created by「 The Khaeng 」on 16 Sep 2018 :)
 */

fun androidx.recyclerview.widget.RecyclerView.turnOffNestedScrollingIfEnoughItems(): Boolean {
    val lm = (layoutManager as androidx.recyclerview.widget.LinearLayoutManager)
    val count = if (lm.itemCount <= 0) 0 else lm.itemCount - 1
    val isFirstVisible = lm.findFirstCompletelyVisibleItemPosition() == 0
    val isLastItemVisible = lm.findLastCompletelyVisibleItemPosition() == count

    isNestedScrollingEnabled = !(isLastItemVisible && isFirstVisible)
    return isNestedScrollingEnabled.not()
}