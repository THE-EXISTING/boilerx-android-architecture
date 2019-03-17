package com.existing.boilerx.core.base.view.holder.base

import android.view.ViewGroup
import com.existing.boilerx.core.R
import com.existing.boilerx.ktx.view.setHeightViewSize
import com.existing.boilerx.ktx.view.show
import kotlinx.android.synthetic.main.holder_space.view.holder_space as space



/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

class SpaceHolder(itemView: ViewGroup)
    : BaseItemViewHolder<SpaceItem>(itemView, R.layout.holder_space) {

    override
    fun onBind(item: SpaceItem) {
        super.onBind(item)
        itemView.space.show(item.isShow)
        if (item.heightPx > 0) {
            itemView.space.setHeightViewSize(item.heightPx)
        }
    }
}
