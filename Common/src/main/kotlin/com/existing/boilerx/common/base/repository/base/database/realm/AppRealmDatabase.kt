package com.existing.boilerx.common.base.repository.base.database.realm

import com.existing.boilerx.realm.BaseRealmDatabase
import com.existing.boilerx.realm.model.PrimitiveTypeDatabaseModule

/**
 * Created by「 The Khaeng 」on 28 Jun 2018 :)
 */
open class AppRealmDatabase : BaseRealmDatabase() {

    fun deleteAllDatabase(): Boolean {
        return deleteAllDatabase(AppDatabaseModule::class.java)
                && deleteAllDatabase(PrimitiveTypeDatabaseModule::class.java)
    }


}