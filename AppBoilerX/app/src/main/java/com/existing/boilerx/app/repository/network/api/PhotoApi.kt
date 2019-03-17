package com.existing.boilerx.app.repository.network.api

import com.existing.boilerx.app.repository.network.model.reponse.PhotoListResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by「 The Khaeng 」on 29 Sep 2017 :)
 */
interface PhotoApi {

    @POST(PhotoApiUrl.URL_LIST)
    fun loadPhotoList(): Single<Response<PhotoListResponse>>

    @POST(PhotoApiUrl.URL_LIST_AFTER)
    fun loadPhotoListAfterId(@Path("id") id: Int): Single<Response<PhotoListResponse>>

    @POST(PhotoApiUrl.URL_LIST_BEFORE)
    fun loadPhotoListBeforeId(@Path("id") id: Int): Single<Response<PhotoListResponse>>
}