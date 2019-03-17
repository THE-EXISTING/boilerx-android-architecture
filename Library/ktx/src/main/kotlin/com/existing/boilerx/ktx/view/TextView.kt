package com.existing.boilerx.ktx.view

import android.content.res.Resources
import androidx.annotation.ColorRes
import android.util.TypedValue
import android.widget.TextView
import com.existing.boilerx.ktx.getColorById
import java.text.NumberFormat
import java.util.*

/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

fun TextView?.setText(number: Int) {
    this?.text = NumberFormat.getIntegerInstance(Locale.US).format(number)
}

fun TextView?.setText(textResId: Int?,
                      text: String?,
                      @ColorRes colorResId: Int? = 0,
                      isVisibleIfAvailable: Boolean = false) {
    if (colorResId != 0) {
        try {
            val typedValue = TypedValue()
            this?.context?.theme?.resolveAttribute(colorResId ?: 0, typedValue, true)
            this?.setTextColor(this.context.getColorById(typedValue.resourceId))
        } catch (e: Resources.NotFoundException) {
            this?.setTextColor(this.context.getColorById(colorResId ?: 0))
        }
    }

    if (text?.isNotEmpty() == true) {
        if (isVisibleIfAvailable) this?.show()
        this?.text = text
    } else if (textResId != null && textResId != 0) {
        if (isVisibleIfAvailable) this?.show()
        this?.text = this?.context?.getString(textResId) ?: ""
    } else {
        if (isVisibleIfAvailable) this?.hide()
    }
}

fun TextView?.setHint(hintResId: Int?, hint: String?) {
    if (hint?.isNotEmpty() == true) {
        this?.hint = hint
    } else if (hintResId != null && hintResId != 0) {
        this?.hint = this?.context?.getString(hintResId) ?: ""
    }
}
