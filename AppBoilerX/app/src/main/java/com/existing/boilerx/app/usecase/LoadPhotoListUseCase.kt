package com.existing.boilerx.app.usecase

import androidx.lifecycle.MediatorLiveData
import com.existing.boilerx.app.repository.PhotoRepository
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.common.base.mvvm.usecase.AppUseCaseLiveData
import com.existing.boilerx.common.base.repository.base.model.AppResult
import com.existing.boilerx.ktx.then
import com.existing.boilerx.ktx.toLiveData
import com.existing.nextwork.engine.NextworkParameter
import com.existing.nextwork.engine.model.NextworkStatus

/**
 * Created by「 The Khaeng 」on 07 Jan 2019 :)
 */
class LoadPhotoListUseCase(
    private var repository: PhotoRepository = PhotoRepository.getInstance()
) : AppUseCaseLiveData<LoadPhotoListUseCase.Parameter, AppResult<List<PhotoModel>>>() {


    override
    fun execute(parameters: Parameter, liveData: MediatorLiveData<AppResult<List<PhotoModel>>>) {
        if (liveData.value?.status == NextworkStatus.COMPLETED || liveData.value == null) {
            repository
                .getPhotoList(parameters)
                .toLiveData()
                .then(liveData) { result ->
                    liveData.value = result
                }
        }
    }

    class Parameter(isForceFetch: Boolean) : NextworkParameter(isForceFetch)

}