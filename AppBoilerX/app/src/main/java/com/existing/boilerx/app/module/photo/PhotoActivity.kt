package com.existing.boilerx.app.module.photo

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.existing.boilerx.app.R
import com.existing.boilerx.common.GlideApp
import com.existing.boilerx.common.base.mvvm.AppMvvmActivity
import com.existing.boilerx.ktx.view.show
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.activity_photo.container_aperture as containerAperture
import kotlinx.android.synthetic.main.activity_photo.container_camera as containerCamera
import kotlinx.android.synthetic.main.activity_photo.container_content as containerContent
import kotlinx.android.synthetic.main.activity_photo.container_focus as containerFocus
import kotlinx.android.synthetic.main.activity_photo.container_iso as containerIso
import kotlinx.android.synthetic.main.activity_photo.container_lens as containerLens
import kotlinx.android.synthetic.main.activity_photo.container_shutter_speed as containerShutterSpeed
import kotlinx.android.synthetic.main.activity_photo.ic_back as icBack
import kotlinx.android.synthetic.main.activity_photo.tv_aperture as tvAperture
import kotlinx.android.synthetic.main.activity_photo.tv_camera as tvCamera
import kotlinx.android.synthetic.main.activity_photo.tv_caption as tvCaption
import kotlinx.android.synthetic.main.activity_photo.tv_focus as tvFocus
import kotlinx.android.synthetic.main.activity_photo.tv_iso as tvIso
import kotlinx.android.synthetic.main.activity_photo.tv_lens as tvLens
import kotlinx.android.synthetic.main.activity_photo.tv_no_content as tvNoContent
import kotlinx.android.synthetic.main.activity_photo.tv_shutter_speed as tvShutterSpeed


/**
 * Created by「 The Khaeng 」on 08 Oct 2017 :)
 */

class PhotoActivity : AppMvvmActivity() {

    companion object {
        const val KEY_PHOTO_ID = ""
    }

    private lateinit var viewModel: PhotoViewModel

    override
    fun onSetupViewModel(savedInstanceState: Bundle?) {
        super.onSetupViewModel(savedInstanceState)
        viewModel = getViewModel(PhotoViewModel::class.java)
    }


    override
    fun setupLayoutView(): Int = R.layout.activity_photo

    override
    fun onIntentExtras(extras: Bundle?, savedInstanceState: Bundle?) {
        super.onIntentExtras(extras, savedInstanceState)
        viewModel.id = extras?.getLong(KEY_PHOTO_ID) ?: throw RuntimeException("Please put KEY_PHOTO_ID before.")
    }

    override fun onInitialize(savedInstanceState: Bundle?) {
        super.onInitialize(savedInstanceState)
        viewModel.loadPhotoModel()
    }

    override
    fun onSetupView(savedInstanceState: Bundle?) {
        super.onSetupView(savedInstanceState)
        icBack.setOnClickListener(onClickListener())
        GlideApp.with(this)
            .load(viewModel.photoModel?.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(photo)

        tvCaption.text = viewModel.photoModel?.caption

        if (viewModel.photoModel?.camera?.isNotBlank() == true) {
            containerCamera.show(true)
            tvCamera.text = viewModel.photoModel?.camera
        }
        if (viewModel.photoModel?.lens?.isNotBlank() == true) {
            containerLens.show(true)
            tvLens.text = viewModel.photoModel?.lens
        }
        if (viewModel.photoModel?.focalLength?.isNotBlank() == true) {
            containerFocus.show(true)
            tvFocus.text = viewModel.photoModel?.focalLength
        }
        if (viewModel.photoModel?.shutterSpeed?.isNotBlank() == true) {
            containerShutterSpeed.show(true)
            tvShutterSpeed.text = viewModel.photoModel?.shutterSpeed
        }
        if (viewModel.photoModel?.aperture?.isNotBlank() == true) {
            containerAperture.show(true)
            tvAperture.text = viewModel.photoModel?.aperture
        }
        if (viewModel.photoModel?.iso?.isNotBlank() == true) {
            containerIso.show(true)
            tvIso.text = viewModel.photoModel?.iso
        }


        if (viewModel.photoModel?.camera?.isBlank() == true
            && viewModel.photoModel?.lens?.isBlank() == true
            && viewModel.photoModel?.focalLength?.isBlank() == true
            && viewModel.photoModel?.shutterSpeed?.isBlank() == true
            && viewModel.photoModel?.aperture?.isBlank() == true
            && viewModel.photoModel?.iso?.isBlank() == true
        ) {
            tvNoContent.show(true)
        } else {
            tvNoContent.show(false)
        }
    }


    override
    fun onBackPressed() {
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onClickListener(): View.OnClickListener = View.OnClickListener { v ->
        when (v) {
            icBack -> finish()
        }
    }


}

