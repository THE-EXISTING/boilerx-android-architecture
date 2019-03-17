package com.existing.boilerx.ktx

import android.os.Parcel
import java.util.*

/**
 * Created by「 The Khaeng 」on 01 Apr 2018 :)
 */

data class KotlinModelExample(
        // Primitive Type
        var varBoolean: Boolean = true,
        var varCharacter: Char = 'a',
        var varByte: Byte = 8,
        var varShort: Short = 20,
        val varInt: Int = 100,
        val varFloat: Float = 10.123f,
        val varDouble: Double = 0.123,
        val varLong: Long = 10000L,
        val varString: String = "KotlinModelExample",

        // List Primitive Type
        var varBooleanList: List<Boolean> = listOf(true, true, true),
        var varCharacterList: List<Char> = listOf('a', 'b', 'c'),
        var varByteList: List<Byte> = listOf(8, 16, 32),
        var varShortList: List<Short> = listOf(20, 21, 22),
        val varIntList: List<Int> = listOf(100, 101, 102),
        val varFloatList: List<Float> = listOf(10.123f, 10.124f),
        val varDoubleList: List<Double> = listOf(0.123, 0.124),
        val varLongList: List<Long> = listOf(10000L, 10001L),
        val varStringList: List<String> = listOf("KotlinModelExample", "KotlinModel2"),

        // Object Type
        var enum: BitCount = BitCount.x16,
        var today: Date = GregorianCalendar(2018, Calendar.APRIL, 1).time,
        var subModel: KotlinSubModelExample = KotlinSubModelExample(),
        var listString: List<String> = listOf("1", "2", "3"),
        var listSubModel: List<KotlinSubModelExample> = listOf(KotlinSubModelExample()),
        var mapString: Map<String, String> = mapOf("key1" to "value1", "key2" to "value2"),
        var listMapString: List<Map<String, String>> = listOf(mapOf("key3" to "value3"), mapOf("key4" to "value4"))
                             ) : KParcelable {

    enum class BitCount constructor(val value: Int) {
        x16(16),
        x32(32),
        x64(64)
    }

    constructor(source: Parcel) : this(
            // Primitive Type
            source.readBoolean(),
            source.readChar(),
            source.readByte(),
            source.readShort(),
            source.readInt(),
            source.readFloat(),
            source.readDouble(),
            source.readLong(),
            source.readString(),
            // List Primitive Type
            source.readBooleanList(),
            source.readCharList(),
            source.readByteList(),
            source.readShortList(),
            source.readIntList(),
            source.readFloatList(),
            source.readDoubleList(),
            source.readLongList(),
            source.readStringList(),
            // Object Type
            source.readEnum<BitCount>() ?: BitCount.x16,
            source.readDate() ?: Date(),
            source.readTypedObjectCompat(KotlinSubModelExample.CREATOR) ?: KotlinSubModelExample(),
            source.createStringArrayList(),
            source.createTypedArrayList(KotlinSubModelExample.CREATOR),
            source.readMapString() ?: mutableMapOf(),
            source.readListMapString() ?: arrayListOf()
    )

    override
    fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        // Primitive Type
        writeBoolean(varBoolean)
        writeChar(varCharacter)
        writeByte(varByte)
        writeShort(varShort)
        writeInt(varInt)
        writeFloat(varFloat)
        writeDouble(varDouble)
        writeLong(varLong)
        writeString(varString)
        // List Primitive Type
        writeBooleanList(varBooleanList)
        writeCharList(varCharacterList)
        writeByteList(varByteList)
        writeShortList(varShortList)
        writeIntList(varIntList)
        writeFloatList(varFloatList)
        writeDoubleList(varDoubleList)
        writeLongList(varLongList)
        writeStringList(varStringList)
        // Object Type
        writeEnum(enum)
        writeDate(today)
        writeTypedObjectCompat(subModel, flags)
        writeStringList(listString)
        writeTypedList(listSubModel)
        writeMapString(mapString)
        writeListMapString(listMapString)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::KotlinModelExample)
    }
}