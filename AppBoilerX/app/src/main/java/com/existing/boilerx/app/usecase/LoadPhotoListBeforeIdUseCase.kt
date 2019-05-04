package com.existing.boilerx.app.usecase

import androidx.lifecycle.MediatorLiveData
import com.existing.nextwork.engine.NextworkParameter
import com.existing.nextwork.engine.model.NextworkStatus
import com.existing.boilerx.app.repository.PhotoRepository
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.common.base.mvvm.usecase.AppUseCaseLiveData
import com.existing.boilerx.common.base.repository.base.model.AppResult
import com.existing.boilerx.ktx.then
import com.existing.boilerx.ktx.toLiveData

/**
 * Created by「 The Khaeng 」on 07 Jan 2019 :)
 */
class LoadPhotoListBeforeIdUseCase(
    private var repository: PhotoRepository = PhotoRepository.getInstance()
) : AppUseCaseLiveData<LoadPhotoListBeforeIdUseCase.Parameter, AppResult<List<PhotoModel>>>() {

    override
    fun execute(parameters: Parameter, liveData: MediatorLiveData<AppResult<List<PhotoModel>>>) {
        if (liveData.value?.status == NextworkStatus.COMPLETED || liveData.value == null) {
            repository
                .getPhotoListBeforeId(parameters)
                .map { resource ->
                    repository.saveMinPhotoId(getMinimumPhotoId(resource.data))
                    resource
                }
                .toLiveData()
                .then(liveData) { result ->
                    liveData.value = result
                }
        }
    }


    fun getMinimumPhotoId(listModel: List<PhotoModel>?): Int {
        if (listModel?.isEmpty() == true) return 0

        var minId = listModel?.get(0)?.databaseId?.toInt() ?: 0

        listModel?.forEach { photoModel ->
            minId = Math.min(minId, photoModel.databaseId.toInt())
        }

        return minId
    }


    class Parameter(
        val id: Int
    ) : NextworkParameter(true)


}