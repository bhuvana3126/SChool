package com.wholesale.jewels.fauxiq.baheekhata.utils

import java.text.DecimalFormat

const val STRING_EMPTY = ""
const val STRING_SPACE = " "
const val STRING_COMMA = ","

object StringFormat {

    @JvmStatic
    fun format(d: Double, type: String): String = DecimalFormat(type).format(d)

    @JvmStatic
    val DECIMAL_FORMAT_000 = "#0.000"

    @JvmStatic
    val DECIMAL_FORMAT_00 = "#0.00"

    @JvmStatic
    val DECIMAL_FORMAT_00000000 = "#0.00000000"
}
