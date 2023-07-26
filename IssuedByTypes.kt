package com.wholesale.jewels.fauxiq.baheekhata.enums

import androidx.room.TypeConverter

class IssuedByConverters {

    companion object {

        @JvmStatic
        @TypeConverter
        fun getId(issuedBy: IssuedBy): String = issuedBy.id

        @JvmStatic
        @TypeConverter
        fun getIssuedBy(input: String): IssuedBy =
            when (input) {
                IssuedBy.OURSELF.id -> IssuedBy.OURSELF
                IssuedBy.KARIGAR.id -> IssuedBy.KARIGAR
                else -> IssuedBy.OURSELF
            }
    }
}

enum class IssuedBy(val id: String, val text: String) {
    OURSELF(id = "O", text = "Ourself"),
    KARIGAR(id = "K", text = "Karigar");

    override fun toString() = text
}

fun getIssuedBy(): ArrayList<IssuedBy> {

    val issuedBy: ArrayList<IssuedBy> = ArrayList()
    issuedBy.add(IssuedBy.OURSELF)
    issuedBy.add(IssuedBy.KARIGAR)

    return issuedBy
}

fun getIssuedBy(issuedBy: String):IssuedBy =
    when (issuedBy) {
        IssuedBy.OURSELF.id -> IssuedBy.OURSELF
        IssuedBy.KARIGAR.id -> IssuedBy.KARIGAR

        else -> IssuedBy.KARIGAR
    }