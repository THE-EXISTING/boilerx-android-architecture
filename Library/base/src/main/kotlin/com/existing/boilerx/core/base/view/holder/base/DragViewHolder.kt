package com.existing.boilerx.core.base.view.holder.base

import android.view.ViewGroup
import com.existing.boilerx.core.base.mvvm.view.adapter.ItemTouchHelperViewHolder

/**
 * Created by「 The Khaeng 」on 23 Aug 2017 :)
 */

abstract class DragViewHolder<I>(parent: ViewGroup, layout: Int)
    : BaseItemViewHolder<I>(parent, layout), ItemTouchHelperViewHolder {


}
