package com.existing.boilerx.common.dialog

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.existing.boilerx.common.dialog.listener.DefaultDialogClickListener
import com.existing.boilerx.common.dialog.listener.DialogDismissListener
import timber.log.Timber

/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

@Suppress("MayBeConstant")
object DialogManager {

    private val TAG_DIALOG = "tag_dialog"


    private var dialog: DialogFragment? = null


    fun showDefaultDialog(
        fragmentManager: FragmentManager,
        @DefaultDialogFragment.Companion.Type type: Int,
        @DrawableRes iconResId: Int = -1,
        @StringRes title: Int = -1,
        @StringRes body: Int = -1,
        @StringRes button: Int = -1,
        data: Bundle? = null,
        tag: String? = null,
        clickListener: DefaultDialogClickListener? = null,
        dismissListener: DialogDismissListener? = null) {
        dismissDialog()
        try {
            dialog = DefaultDialogFragment.Builder()
                    .setType(type)
                    .setIcon(iconResId)
                    .setTitle(title)
                    .setButton(button)
                    .setBody(body)
                    .setData(data)
                    .setDefalutClickListener(clickListener)
                    .setDialogDismissListener(dismissListener)
                    .build()
            dialog?.show(fragmentManager, tag ?: TAG_DIALOG)

        } catch (e: Exception) {
            Timber.e(e)
        }
    }




    fun dismissDialog() {
        try {
            if (dialog?.isAdded == true) {
                dialog?.dismiss()
                dialog = null
            }

        } catch (e: Exception) {
            Timber.e(e)
        }
    }



}
