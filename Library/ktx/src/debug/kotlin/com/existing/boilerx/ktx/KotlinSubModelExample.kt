package com.existing.boilerx.ktx

import android.os.Parcel

/**
 * Created by「 The Khaeng 」on 01 Apr 2018 :)
 */
class KotlinSubModelExample(
        val variable1: String = "",
        val variable2: Float = 0f,
        val variable3: Int = 0,
        val variable4: Long = 0L
                           ) : KParcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readFloat(),
            source.readInt(),
            source.readLong()
    )

    override
    fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(variable1)
        writeFloat(variable2)
        writeInt(variable3)
        writeLong(variable4)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::KotlinSubModelExample)
    }
}