package com.existing.boilerx.realm

import com.existing.boilerx.realm.model.BigDecimalRealmObject
import com.existing.boilerx.realm.model.BigIntegerRealmObject
import com.existing.boilerx.realm.model.BooleanRealmObject
import com.existing.boilerx.realm.model.StringRealmObject
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.RealmModule
import timber.log.Timber
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * Created by「 The Khaeng 」on 11 Oct 2017 :)
 */
abstract class BaseRealmDatabase {


    fun createId(): String = UUID.randomUUID().toString()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> deleteAllDatabase(moduleClass: Class<T>): Boolean {
        val annotation = moduleClass.annotations
                .firstOrNull { it.annotationClass.java.name == RealmModule::class.java.name }

        if (annotation != null) {
            Timber.i("Got annotation in module $annotation")
            val moduleAnnotation = annotation as RealmModule
            moduleAnnotation.classes
                    .filter { clazz -> clazz.java.interfaces.contains(RealmModel::class.java) }
                    .forEach { clazz -> (clazz.java as Class<RealmModel>).newInstance().deleteAll() }
            moduleAnnotation.classes
                    .filter { clazz -> clazz.java.superclass == RealmObject::class.java }
                    .forEach { clazz -> (clazz.java as Class<RealmObject>).newInstance().deleteAll() }

            return true
        }
        return false
    }


    fun saveString(id: String, value: String) {
        StringRealmObject(id, value).save()
    }

    fun loadString(id: String, defaultValue: String = ""): String {
        return StringRealmObject()
                .queryFirst { equalTo(StringRealmObject.FIELD_ID_NAME, id) }?.value ?: defaultValue
    }

    fun saveBoolean(id: String, value: Boolean) {
        BooleanRealmObject(id, value).save()
    }

    fun loadBoolean(id: String, defaultValue: Boolean = false): Boolean {
        return BooleanRealmObject()
                .queryFirst { equalTo(StringRealmObject.FIELD_ID_NAME, id) }?.value ?: defaultValue
    }


    fun saveInt(id:String, value: Int){
        BigIntegerRealmObject(id, value).save()
    }

    fun loadInt(id: String, defaultValue: Int = 0): Int {
        return BigIntegerRealmObject()
                .queryFirst { equalTo(BigIntegerRealmObject.FIELD_ID_NAME, id) }?.value?.toInt() ?: defaultValue
    }

    fun saveLong(id:String, value: Long){
        BigIntegerRealmObject(id, value).save()
    }

    fun loadLong(id: String, defaultValue: Long = 0): Long {
        return BigIntegerRealmObject()
                .queryFirst { equalTo(BigIntegerRealmObject.FIELD_ID_NAME, id) }?.value?.toLong() ?: defaultValue
    }

    fun saveBigInteger(id:String, value: BigInteger){
        BigIntegerRealmObject(id, value).save()
    }

    fun loadBigInteger(id: String, defaultValue: BigInteger = BigInteger.ZERO): BigInteger {
        return BigIntegerRealmObject()
                .queryFirst { equalTo(BigIntegerRealmObject.FIELD_ID_NAME, id) }?.value ?: defaultValue
    }

    fun saveFloat(id:String, value: Float){
        BigDecimalRealmObject(id, value).save()
    }

    fun loadFloat(id: String, defaultValue: Float = 0.0f): Float {
        return BigDecimalRealmObject()
                .queryFirst { equalTo(BigIntegerRealmObject.FIELD_ID_NAME, id) }?.value?.toFloat() ?: defaultValue
    }

    fun saveDouble(id:String, value: Double){
        BigDecimalRealmObject(id, value).save()
    }

    fun loadDouble(id: String, defaultValue: Double = 0.0): Double {
        return BigDecimalRealmObject()
                .queryFirst { equalTo(BigIntegerRealmObject.FIELD_ID_NAME, id) }?.value?.toDouble() ?: defaultValue
    }

    fun saveBigDecimal(id:String, value: BigDecimal){
        BigDecimalRealmObject(id, value).save()
    }

    fun loadBigDecimal(id: String, defaultValue: BigDecimal = BigDecimal.ZERO): BigDecimal {
        return BigDecimalRealmObject()
                .queryFirst { equalTo(BigIntegerRealmObject.FIELD_ID_NAME, id) }?.value ?: defaultValue
    }

}