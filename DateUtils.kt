package com.wholesale.jewels.fauxiq.baheekhata.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    enum class Day(val id: String, val text: String, val value: Int) {
        TODAY(id = "T", text = "Today", value = 0),
        TOMORROW(id = "TO", text = "Tomorrow", value = 1),
        YESTERDAY(id = "Y", text = "Yesterday", value = -1);

        override fun toString() = text
    }

    fun getDate(): Date {
        return Calendar.getInstance().time
    }

    fun getDate(day: Day): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, day.value)

        return calendar.time
    }

    val currentDate: Long
        get() {
            return Calendar.getInstance().time.time
        }

    val tomorrowDate: Long
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            return calendar.time.time
        }
}

fun Date.tomorrow(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

fun Date.yesterday(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return calendar.time
}

val Date.startDate: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

val Date.startDateInMillis: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time
    }

val Long.startDateInMillis: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time
    }

val Date.endDate: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

val Date.endDateInMillis: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time.time
    }

val Long.endDateInMillis: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time.time
    }

val Long.string_dd_MMM_yyyy: String?
    get() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this

        val sdf = SimpleDateFormat(Utils.DATE_FORMAT_dd_MMM_yyyy, Locale.ENGLISH)
        return sdf.format(calendar.time)
    }