package com.existing.boilerx.common.base.repository.base.database.realm

import android.content.Context
import android.content.pm.PackageManager
import com.existing.boilerx.realm.RealmConfigStore
import com.existing.boilerx.realm.model.PrimitiveTypeDatabaseModule
import io.realm.Realm
import io.realm.RealmConfiguration

object RealmConfig {

    private lateinit var config: RealmConfiguration


    fun init(
        context: Context,
        module: Any,
        version: Int
    ): Boolean {

        Realm.init(context)

        config = RealmConfiguration.Builder()
            .name("boilerx.realm")
            .modules(module, PrimitiveTypeDatabaseModule())
            .encryptionKey(getRealmEncryptedKey(context))
            .schemaVersion(version.toLong())
            .build()

        RealmConfigStore
            .initModule(config, module::class.java, PrimitiveTypeDatabaseModule::class.java)

        return true
    }

    private fun getRealmEncryptedKey(context: Context): ByteArray {
        val encryptedKey = StringBuilder()
        val valueId = getInstalledTime(context)
        while (encryptedKey.length < RealmConfiguration.KEY_LENGTH) {
            encryptedKey.append(valueId)
            if (encryptedKey.length < RealmConfiguration.KEY_LENGTH) {
                encryptedKey.append("|")
            }
        }
        return encryptedKey.substring(0, RealmConfiguration.KEY_LENGTH).toByteArray()
    }

    private fun getInstalledTime(context: Context): String {
        try {
            return context
                .packageManager
                .getPackageInfo(context.packageName, 0).firstInstallTime.toString()
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        return "N/A"
    }


}
