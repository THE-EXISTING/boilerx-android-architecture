package com.existing.boilerx.app.repository.network

import com.existing.nextwork.engine.checker.ResponseResultChecker
import com.existing.nextwork.operator.NextworkLogError
import com.existing.boilerx.app.repository.network.api.PhotoApi
import com.existing.boilerx.app.repository.network.api.PhotoApiService
import com.existing.boilerx.app.repository.network.model.reponse.PhotoListResponse
import com.existing.boilerx.common.base.repository.base.ConvertToAppResponse
import com.existing.boilerx.common.base.repository.base.model.AppResponse
import io.reactivex.Flowable

/**
 * Created by「 The Khaeng 」on 12 Oct 2017 :)
 */

class PhotoApiManager(var mockApi: PhotoApi? = null) {

    companion object {

        @Volatile
        private var INSTANCE: PhotoApiManager? = null

        fun getInstance(mockApi: PhotoApi? = null): PhotoApiManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PhotoApiManager(mockApi).also { INSTANCE = it }
            }

        fun newInstance(mockApi: PhotoApi? = null): PhotoApiManager =
            PhotoApiManager(mockApi)
    }


    fun getPhotoList(): Flowable<AppResponse<PhotoListResponse>> {
        return PhotoApiService.newInstance
            .createApi()
            .loadPhotoList()
            .compose(ResponseResultChecker<PhotoListResponse>())
            .onErrorResumeNext(NextworkLogError())
            .compose(ConvertToAppResponse<AppResponse<PhotoListResponse>>())
            .toFlowable()
    }

    fun getPhotoListAfterId(id: Int): Flowable<AppResponse<PhotoListResponse>> {
        return PhotoApiService.newInstance
            .createApi()
            .loadPhotoListAfterId(id)
            .compose(ResponseResultChecker<PhotoListResponse>())
            .onErrorResumeNext(NextworkLogError())
            .compose(ConvertToAppResponse<AppResponse<PhotoListResponse>>())
            .toFlowable()
    }

    fun getPhotoListBeforeId(id: Int): Flowable<AppResponse<PhotoListResponse>> {
        return PhotoApiService.newInstance
            .createApi()
            .loadPhotoListBeforeId(id)
            .compose(ResponseResultChecker<PhotoListResponse>())
            .onErrorResumeNext(NextworkLogError())
            .compose(ConvertToAppResponse<AppResponse<PhotoListResponse>>())
            .toFlowable()
    }

}
