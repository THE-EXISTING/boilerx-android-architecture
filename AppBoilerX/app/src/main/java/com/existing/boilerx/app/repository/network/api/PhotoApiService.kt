package com.existing.boilerx.app.repository.network.api

import com.existing.boilerx.common.base.repository.base.network.AppApiCreator


/**
 * Created by「 The Khaeng 」on 29 Sep 2017 :)
 */


class PhotoApiService private constructor()
    : AppApiCreator<PhotoApi>(PhotoApi::class.java) {

    companion object {
        val newInstance
            get() = PhotoApiService()
    }
}