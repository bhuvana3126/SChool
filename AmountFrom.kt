package com.wholesale.jewels.fauxiq.baheekhata.enums

import androidx.room.TypeConverter

enum class AmountFrom(val id: String, val text: String) {
    FROM_MONEY_IO("M", "Money-io"),
    FROM_FINE_IO("F", "Fine-io");
}

class AmountFromConverters {

    companion object {

        @JvmStatic
        @TypeConverter
        fun getId(amountFrom: AmountFrom): String = amountFrom.id

        @JvmStatic
        @TypeConverter
        fun getAmountFrom(input: String): AmountFrom =
            when (input) {
                AmountFrom.FROM_MONEY_IO.id -> AmountFrom.FROM_MONEY_IO
                AmountFrom.FROM_FINE_IO.id -> AmountFrom.FROM_FINE_IO
                else -> AmountFrom.FROM_MONEY_IO
            }
    }
}