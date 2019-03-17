package com.existing.boilerx.app.repository.database

import android.content.Context
import com.existing.boilerx.app.repository.ModelConverter
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.common.base.repository.base.database.realm.AppDatabaseModule
import com.existing.boilerx.common.base.repository.base.database.realm.AppRealmDatabase
import com.existing.boilerx.common.base.repository.base.database.realm.RealmConfig
import com.existing.boilerx.common.base.repository.base.database.realm.model.PhotoRealmObject
import com.existing.boilerx.realm.deleteAll
import com.existing.boilerx.realm.queryAllAsSingle
import com.existing.boilerx.realm.queryFirst
import com.existing.boilerx.realm.saveAll
import io.reactivex.Flowable


class AppDatabase private constructor() : AppRealmDatabase() {

    private object Holder {
        val INSTANCE = AppDatabase()

    }

    companion object {
        const val DATABASE_VERSION = 1

        private const val MAX_PHOTO_ID = "max_photo_id"
        private const val MIN_PHOTO_ID = "min_photo_id"

        val INSTANCE: AppDatabase by lazy { Holder.INSTANCE }

        fun initDatabase(context: Context): Boolean {
            return RealmConfig.init(
                context,
                AppDatabaseModule(),
                DATABASE_VERSION
            )
        }
    }

    fun saveMaxPhotoId(id: Int) {
        saveInt(MAX_PHOTO_ID, id)
    }

    fun loadMaxPhotoId(): Int = loadInt(MAX_PHOTO_ID, -1)

    fun saveMinPhotoId(id: Int) {
        saveInt(MIN_PHOTO_ID, id)
    }

    fun loadMinPhotoId(): Int = loadInt(MIN_PHOTO_ID, -1)

    fun deleteAllPhotoItemList() {
        PhotoRealmObject().deleteAll()
    }


    fun savePhotoItemList(photoList: List<PhotoModel>) {
        photoList
            .map {
                ModelConverter.photoToRealmObject(it)
            }.saveAll()
    }

    fun loadPhotoModel(id: String): PhotoModel? {
        return PhotoRealmObject()
            .queryFirst { equalTo(PhotoRealmObject.FIELD_ID_NAME, id) }
            ?.let {
                ModelConverter.photoRealmToModel(it)
            }
    }

    fun loadPhotoItemList(): Flowable<List<PhotoModel>> {
        return PhotoRealmObject()
            .queryAllAsSingle()
            .map { ModelConverter.photoRealmListToModelList(it) }
            .toFlowable()
    }



}


