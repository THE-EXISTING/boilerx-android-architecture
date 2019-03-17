package com.existing.boilerx.app.repository

import com.existing.boilerx.app.repository.network.model.reponse.PhotoResponse
import com.existing.boilerx.app.repository.network.model.reponse.PhotoListResponse
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.common.base.repository.base.database.realm.model.PhotoRealmObject
import com.existing.boilerx.ktx.notnull

/**
 * Created by「 The Khaeng 」on 13 Jun 2018 :)
 */
object ModelConverter {

    fun photoToRealmObject(model: PhotoModel): PhotoRealmObject =
            PhotoRealmObject().apply {
                id = model.databaseId
                imageWidthPortrait = model.imageWidthPortrait
                imageHeightPortrait = model.imageHeightPortrait
                imageWidthLandScape = model.imageHeightPortrait
                imageHeightLandScape = model.imageHeightLandScape
                imageUrl = model.imageUrl
                caption = model.caption
                profilePicture = model.profilePicture
                camera = model.camera
                lens = model.lens
                focalLength = model.focalLength
                iso = model.iso
                shutterSpeed = model.shutterSpeed
                aperture = model.aperture
            }

    fun photoResponseToModel(response: PhotoResponse): PhotoModel =
            PhotoModel(response.id.toString()).apply {
                imageWidthPortrait = 0
                imageHeightPortrait = 0
                imageWidthLandScape = 0
                imageHeightLandScape = 0
                imageUrl = response.imageUrl.notnull()
                caption = response.caption.notnull()
                profilePicture = response.profilePicture.notnull()
                camera = response.camera.notnull()
                lens = response.lens.notnull()
                focalLength = response.focalLength.notnull()
                iso = response.iso.notnull()
                shutterSpeed = response.shutterSpeed.notnull()
                aperture = response.aperture.notnull()
            }

    fun photoRealmToModel(realm: PhotoRealmObject): PhotoModel =
            PhotoModel(realm.id).apply {
                imageWidthPortrait = realm.imageWidthPortrait
                imageHeightPortrait = realm.imageHeightPortrait
                imageWidthLandScape = realm.imageHeightPortrait
                imageHeightLandScape = realm.imageHeightLandScape
                imageUrl = realm.imageUrl
                caption = realm.caption
                profilePicture = realm.profilePicture
                camera = realm.camera
                lens = realm.lens
                focalLength = realm.focalLength
                iso = realm.iso
                shutterSpeed = realm.shutterSpeed
                aperture = realm.aperture
            }

    fun photoRealmListToModelList(realm: List<PhotoRealmObject>): List<PhotoModel> =
            realm.map { realm -> photoRealmToModel(realm) }

    fun photoResponseListToModelList(responseList: PhotoListResponse?): List<PhotoModel> =
            responseList?.data?.map { response -> photoResponseToModel(response) } ?: listOf()


}