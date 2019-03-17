package com.existing.boilerx.common.widgets

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.existing.boilerx.common.GlideApp
import com.existing.boilerx.core.base.view.holder.base.BaseItemViewHolder

/**
 * Created by「 The Khaeng 」on 23 Aug 2017 :)
 */

abstract class AppViewHolder<I>(parent: ViewGroup, layout: Int)
    : BaseItemViewHolder<I>(parent, layout){


    fun setImage(imgView: ImageView,
                 url: String,
                 @DrawableRes placeholderId: Int) {
        GlideApp.with(itemView.context)
                .load(url)
                .placeholder(placeholderId)
                .centerCrop()
                .into(imgView)
    }

    fun setCircleImage(imgView: ImageView,
                       url: String,
                       @DrawableRes placeholderId: Int) {
        GlideApp.with(itemView.context)
                .load(url)
                .centerCrop()
                .placeholder(placeholderId)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imgView)
    }

    fun setImage(imgView: ImageView,
                 transformation: Transformation<Bitmap>,
                 url: String,
                 @DrawableRes placeholderId: Int) {
        GlideApp.with(itemView.context)
                .load(url)
                .centerCrop()
                .placeholder(placeholderId)
                .transform(transformation)
                .into(imgView)
    }

}
