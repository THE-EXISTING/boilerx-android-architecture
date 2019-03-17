package com.existing.boilerx.app.module.photo


import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.app.usecase.LoadPhotoUseCase
import com.existing.boilerx.common.base.mvvm.AppViewModel

class PhotoViewModel(
    private var loadPhotoUseCase: LoadPhotoUseCase = LoadPhotoUseCase(),
    var id: Long = 0,
    var photoModel: PhotoModel? = null
) : AppViewModel() {


    fun loadPhotoModel() {
        photoModel = loadPhotoUseCase.executeNow(id)
    }
}
