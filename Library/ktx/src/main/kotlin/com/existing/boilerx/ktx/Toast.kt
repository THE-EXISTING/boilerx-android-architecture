package com.existing.boilerx.ktx

import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

fun Context.toast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Context.toastLong(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun View.toast(resId: Int) = context.toast(resId)

fun View.toast(text: String) = context.toast(text)

fun View.toastLong(resId: Int) = context.toastLong(resId)

fun View.toastLong(text: String) = context.toastLong(text)

fun androidx.fragment.app.Fragment.toast(resId: Int) = activity?.toast(resId)

fun androidx.fragment.app.Fragment.toast(text: String) = activity?.toast(text)

fun androidx.fragment.app.Fragment.toastLong(resId: Int) = activity?.toastLong(resId)

fun androidx.fragment.app.Fragment.toastLong(text: String) = activity?.toastLong(text)

