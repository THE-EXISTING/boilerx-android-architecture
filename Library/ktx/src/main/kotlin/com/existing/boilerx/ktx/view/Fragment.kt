package com.existing.boilerx.ktx.view

import androidx.annotation.DimenRes
import com.existing.boilerx.ktx.getFloatDimen

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

fun androidx.fragment.app.Fragment.getFloatDimen(@DimenRes resId: Int): Float? {
    return this.context?.getFloatDimen(resId)
}

