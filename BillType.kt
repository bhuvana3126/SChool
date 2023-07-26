package com.wholesale.jewels.fauxiq.baheekhata.enums

import androidx.room.TypeConverter

class BillTypeConverters {

    companion object {

        @JvmStatic
        @TypeConverter
        fun getId(billType: BillType): String = billType.id

        @JvmStatic
        @TypeConverter
        fun getPeople(input: String): BillType = getBillType(input)
    }
}

enum class BillType(val id: String, val text: String) {
    RATE_BASED(id = "R", text = "Rate Based"),
    FINE_BASED(id = "F", text = "Fine Based"),
    NONE(id = "N", text = "None");

    override fun toString() = text
}

fun getBillType(billType: String) =
    when (billType) {
        BillType.RATE_BASED.id -> BillType.RATE_BASED
        BillType.FINE_BASED.id -> BillType.FINE_BASED
        else -> BillType.NONE
    }