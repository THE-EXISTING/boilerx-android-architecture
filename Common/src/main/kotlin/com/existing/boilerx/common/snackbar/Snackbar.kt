package com.existing.boilerx.common.snackbar

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by「 The Khaeng 」on 21 Oct 2017 :)
 */


fun FragmentActivity.showSnackbarSuccess(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarSuccess(targetView
                                                    ?: this.findViewById(android.R.id.content), message)

fun Fragment.showSnackbarSuccess(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarSuccess(targetView ?: this.view, message)

fun FragmentActivity.showSnackbarCaution(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarCaution(targetView
                                                    ?: this.findViewById(android.R.id.content), message)

fun Fragment.showSnackbarCaution(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarCaution(targetView ?: this.view, message)

fun FragmentActivity.showSnackbarInfo(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarInfo(targetView
                                                 ?: this.findViewById(android.R.id.content), message)

fun Fragment.showSnackbarInfo(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarInfo(targetView ?: this.view, message)


fun FragmentActivity.showSnackbarError(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarError(targetView
                                                  ?: this.findViewById(android.R.id.content), message)

fun Fragment.showSnackbarError(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarError(targetView ?: this.view, message)

fun FragmentActivity.showSnackbarErrorDismiss(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarErrorDismiss(targetView
                                                         ?: this.findViewById(android.R.id.content), message)

fun Fragment.showSnackbarErrorDismiss(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarErrorDismiss(targetView ?: this.view, message)

fun FragmentActivity.showSnackbarMessage(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarMessage(targetView
                                                    ?: this.findViewById(android.R.id.content), message)

fun Fragment.showSnackbarMessage(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarMessage(targetView ?: this.view, message)

fun FragmentActivity.showSnackbarMessageDismiss(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarMessageDismiss(targetView
                                                           ?: this.findViewById(android.R.id.content), message)

fun Fragment.showSnackbarMessageDismiss(message: String, targetView: View? = null) =
        SnackbarManager.showSnackbarMessageDismiss(targetView ?: this.view, message)



