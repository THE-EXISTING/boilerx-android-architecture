package com.existing.boilerx.app.repository.model

import android.os.Parcel
import com.existing.boilerx.ktx.KParcelable
import com.existing.boilerx.ktx.notnull
import com.existing.boilerx.ktx.parcelableCreator
import com.existing.boilerx.realm.DatabaseModel

/**
 * Created by「 The Khaeng 」on 07 Jan 2019 :)
 */
class PhotoModel(
        id: String,
        var imageWidthPortrait: Int = 0,
        var imageHeightPortrait: Int = 0,
        var imageWidthLandScape: Int = 0,
        var imageHeightLandScape: Int = 0,
        var imageUrl: String = "",
        var caption: String = "",
        var profilePicture: String = "",
        var camera: String = "",
        var lens: String = "",
        var focalLength: String = "",
        var iso: String = "",
        var shutterSpeed: String = "",
        var aperture: String = "") : DatabaseModel(id), KParcelable {

    constructor(source: Parcel) : this(
            source.readString().notnull(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString().notnull(),
            source.readString().notnull(),
            source.readString().notnull(),
            source.readString().notnull(),
            source.readString().notnull(),
            source.readString().notnull(),
            source.readString().notnull(),
            source.readString().notnull(),
            source.readString().notnull()
                                      )

    override
    fun describeContents() = 0

    override
    fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(databaseId)
        writeInt(imageWidthPortrait)
        writeInt(imageHeightPortrait)
        writeInt(imageWidthLandScape)
        writeInt(imageHeightLandScape)
        writeString(imageUrl)
        writeString(caption)
        writeString(profilePicture)
        writeString(camera)
        writeString(lens)
        writeString(focalLength)
        writeString(iso)
        writeString(shutterSpeed)
        writeString(aperture)
    }

    override
    fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as PhotoModel

        if (imageWidthPortrait != other.imageWidthPortrait) return false
        if (imageHeightPortrait != other.imageHeightPortrait) return false
        if (imageWidthLandScape != other.imageWidthLandScape) return false
        if (imageHeightLandScape != other.imageHeightLandScape) return false
        if (imageUrl != other.imageUrl) return false
        if (caption != other.caption) return false
        if (profilePicture != other.profilePicture) return false
        if (camera != other.camera) return false
        if (lens != other.lens) return false
        if (focalLength != other.focalLength) return false
        if (iso != other.iso) return false
        if (shutterSpeed != other.shutterSpeed) return false
        if (aperture != other.aperture) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + imageWidthPortrait
        result = 31 * result + imageHeightPortrait
        result = 31 * result + imageWidthLandScape
        result = 31 * result + imageHeightLandScape
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + caption.hashCode()
        result = 31 * result + profilePicture.hashCode()
        result = 31 * result + camera.hashCode()
        result = 31 * result + lens.hashCode()
        result = 31 * result + focalLength.hashCode()
        result = 31 * result + iso.hashCode()
        result = 31 * result + shutterSpeed.hashCode()
        result = 31 * result + aperture.hashCode()
        return result
    }


    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::PhotoModel)
    }
}