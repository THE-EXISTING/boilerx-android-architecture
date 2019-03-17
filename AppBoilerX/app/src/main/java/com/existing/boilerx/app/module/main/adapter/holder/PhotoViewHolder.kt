package com.existing.boilerx.app.module.main.adapter.holder

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.existing.boilerx.app.R
import com.existing.boilerx.app.module.main.adapter.item.PhotoItem
import com.existing.boilerx.common.GlideApp
import com.existing.boilerx.core.base.view.holder.base.BaseItemViewHolder
import com.existing.boilerx.ktx.view.show
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.view_holder_picture.view.*


/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

class PhotoViewHolder(parent: ViewGroup) : BaseItemViewHolder<PhotoItem>(parent, R.layout.view_holder_picture) {


    @SuppressLint("CheckResult")
    override
    fun onBind(item: PhotoItem) {
        super.onBind(item)
        itemView.apply {
            title.show(item.model.caption != "Untitled")
            title.text = item.model.caption

            GlideApp.with(context)
                .load(item.model.imageUrl)
                .apply {
                    if (isPortrait() && item.model.imageHeightPortrait != 0 || item.model.imageWidthPortrait != 0) {
                        override(item.model.imageWidthPortrait, item.model.imageHeightPortrait)
                    } else if (!isPortrait() && item.model.imageHeightLandScape != 0 || item.model.imageWidthLandScape != 0) {
                        override(item.model.imageWidthLandScape, item.model.imageHeightLandScape)
                    }
                }
                .placeholder(R.drawable.img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(requestListener)
                .into(itemView.image)

            PushDownAnim
                .setPushDownAnimTo(itemView)
                .setScale(0.98f)

        }


    }

    /* =========================== Private method =============================================== */

    private fun isPortrait(): Boolean =
        context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    private val requestListener: RequestListener<Drawable> =
        object : RequestListener<Drawable> {

            @SuppressLint("SetTextI18n")
            override
            fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?, isFirstResource: Boolean
            ): Boolean {
                itemView.apply {
                    resource?.let {
                        val glideTarget = target as DrawableImageViewTarget
                        val iv = glideTarget.view
                        val width = iv.measuredWidth

                        val targetHeight = width * resource.intrinsicHeight / resource.intrinsicWidth
                        if (width != 0 && targetHeight != 0) {
                            if (image.layoutParams.height != targetHeight) {
                                if (isPortrait()) {
                                    item?.model?.imageWidthPortrait = width
                                    item?.model?.imageHeightPortrait = targetHeight
                                    if (image.layoutParams.height != item?.model?.imageHeightPortrait) {
                                        image.layoutParams.height = targetHeight
                                    }
                                } else {
                                    item?.model?.imageWidthLandScape = width
                                    item?.model?.imageHeightLandScape = targetHeight
                                    if (itemView.image.layoutParams.height != item?.model?.imageHeightLandScape) {
                                        image.layoutParams.height = targetHeight
                                    }
                                }
                                image.requestLayout()
                            }
                        } else {
                            itemView.image.layout(0, 0, 0, 0)
                        }
                        itemView.size.visibility = View.VISIBLE
                        itemView.size.text = "$width x $targetHeight"
                    }

                }
                return false
            }

            override
            fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }


}
