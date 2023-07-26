package com.wholesale.jewels.fauxiq.baheekhata.utils

import java.text.DecimalFormat

object DecimalFormat {

    @JvmStatic
    fun round(value: Double, decimal: Int): Double =
        Math.round(value * decimal).toDouble() / decimal

    @JvmStatic
    val DECIMAL_FORMAT_000 = 1000

    @JvmStatic
    val DECIMAL_FORMAT_00 = 100
}
