package com.existing.boilerx.app.usecase

import com.existing.boilerx.app.repository.PhotoRepository
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.common.base.mvvm.usecase.AppUseCase

/**
 * Created by「 The Khaeng 」on 07 Jan 2019 :)
 */
class LoadPhotoUseCase(
    private var repository: PhotoRepository = PhotoRepository.getInstance()
) : AppUseCase<Long, PhotoModel?>() {

    override
    fun execute(parameter: Long): PhotoModel? = repository.getPhotoModel(parameter)

}