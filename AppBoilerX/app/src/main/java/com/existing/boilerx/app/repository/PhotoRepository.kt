package com.existing.boilerx.app.repository

import com.existing.boilerx.app.repository.database.AppDatabase
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.app.repository.network.PhotoApiManager
import com.existing.boilerx.app.usecase.LoadPhotoListAfterIdUseCase
import com.existing.boilerx.app.usecase.LoadPhotoListBeforeIdUseCase
import com.existing.boilerx.app.usecase.LoadPhotoListUseCase
import com.existing.boilerx.common.base.repository.base.DefaultNetworkBoundResource
import com.existing.boilerx.common.base.repository.base.DefaultNetworkCacheBoundResource
import com.existing.boilerx.common.base.repository.base.DefaultRepository
import com.existing.boilerx.common.base.repository.base.model.AppResult
import com.existing.nextwork.engine.AppExecutors
import io.reactivex.Flowable

/**
 * Created by「 The Khaeng 」on 12 Oct 2017 :)
 */

class PhotoRepository
private constructor(
    appExecutors: AppExecutors,
    val database: AppDatabase = AppDatabase.INSTANCE,
    private val serviceManager: PhotoApiManager = PhotoApiManager.newInstance()
) : DefaultRepository(appExecutors) {


    companion object {
        @Volatile
        private var INSTANCE: PhotoRepository? = null

        fun getInstance(appExecutors: AppExecutors = AppExecutors.getInstance()): PhotoRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PhotoRepository(appExecutors).also { INSTANCE = it }
            }

        fun newInstance(appExecutors: AppExecutors = AppExecutors.getInstance()): PhotoRepository =
            PhotoRepository(appExecutors)
    }


    fun saveMaxPhotoId(id: Int) {
        database.saveMaxPhotoId(id)
    }

    fun saveMinPhotoId(id: Int) {
        database.saveMinPhotoId(id)
    }


    fun getMaxPhotoId(): Int = database.loadMaxPhotoId()

    fun savePhotoListItemAll(photoList: MutableList<PhotoModel>) {
        database.savePhotoItemList(photoList)
    }

    fun getPhotoModel(id: Long): PhotoModel? = database.loadPhotoModel(id.toString())


    fun getPhotoList(trigger: LoadPhotoListUseCase.Parameter): Flowable<AppResult<List<PhotoModel>>> =
        DefaultNetworkCacheBoundResource(
            appExecutors,
            loadFromDb = {
                database.loadPhotoItemList()
            },
            onSaveCallResult = { item ->
                database.savePhotoItemList(item)
            },
            onShouldFetch = { data ->
                trigger.isForceFetch || data.isEmpty()
            },
            createCall = { serviceManager.getPhotoList() },
            onConvertToResultType = { response ->
                ModelConverter.photoResponseListToModelList(response)
            }
        ).toFlowable()


    fun getPhotoListAfterId(trigger: LoadPhotoListAfterIdUseCase.Parameter): Flowable<AppResult<List<PhotoModel>>> =
        DefaultNetworkBoundResource(
            appExecutors,
            onSaveCallResult = { item ->
                database.savePhotoItemList(item)
            },
            createCall = {
                serviceManager.getPhotoListAfterId(trigger.id)
            },
            onConvertToResultType = { response ->
                ModelConverter.photoResponseListToModelList(response)
            }).toFlowable()

    fun getPhotoListBeforeId(trigger: LoadPhotoListBeforeIdUseCase.Parameter): Flowable<AppResult<List<PhotoModel>>> =
        DefaultNetworkBoundResource(
            appExecutors,
            onSaveCallResult = { item ->
                database.savePhotoItemList(item)
            },
            createCall = {
                serviceManager.getPhotoListBeforeId(trigger.id)
            },
            onConvertToResultType = { response ->
                ModelConverter.photoResponseListToModelList(response)
            }).toFlowable()


    fun deleteAllDatabase(): Boolean = database.deleteAllDatabase()


}
