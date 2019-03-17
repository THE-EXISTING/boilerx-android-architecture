@file:JvmName("FontSizeUtility")
package com.existing.boilerx.common.base.extension

import android.util.TypedValue
import android.widget.TextView
import com.thekhaeng.materialdesign.metric.isTablet
import com.thekhaeng.materialdesign.metric.pxToSp
import java.util.*

/**
 * Created by「 The Khaeng 」on 25 Oct 2017 :)
 */

fun TextView.calculateFontSize() {
    if (!this.isInEditMode) {
        if ("th" == Locale.getDefault().language) {
            this.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    pxToSp(this.textSize) + 1.0f)
        }

        if (this.context.isTablet) {
            this.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    pxToSp(this.textSize) + 2.0f)
        }
    }
}