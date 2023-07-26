package com.wholesale.jewels.fauxiq.baheekhata.utils

import android.os.Environment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.io.File
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

interface OnTextChangeListener {
    fun onTextChange(text: CharSequence)
}

interface OnTextViewTextChangeListener {
    fun onTextChange(view: TextView, text: CharSequence)
}

interface OnCheckedChangeListener {
    fun onCheckedChange(id: Int, isChecked: Boolean)
}

fun EditText.backSpace() {
    this.setText(if (text.isNotEmpty()) text.subSequence(0, text.length - 1) else null)
    this.setSelection(text.length)
}

fun TextView.getDouble(): Double {
    println("TextView.getDouble(${this.text})")
    return if (this.text?.length == 0 || this.text == "." || this.text == "\u20B9")
        0.0 else {
        this.text
            .toString()
            .replace(" ", "")
            .replace("\u20B9", "")
            .replace(".", (if (this.text.toString().toCharArray()[0] == '.') "0." else "."))
            .toDouble()
    }
}

fun TextView.getLong(): Long {
    return if (this.text?.length == 0 || this.text == "." || this.text == "\u20B9")
        0L else
        this.text
            .toString()
            .replace(" ", "")
            .replace("\u20B9", "")
            .toLong()
}

fun TextView.getInt(): Int? {
    return this.text?.let {
        if (this.text.isNotEmpty())
            this.text.toString().toInt()
        else null
    }
}

fun TextView.getString(): String {
    return this.text?.let {
        this.text.toString()
    } ?: ""
}

fun TextView.isNotEmpty(): Boolean {
    return this.text?.length != 0
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun CharSequence.getDouble(): Double {

    return try {
        if (this.toString().isEmpty() || this.toString() == "." || this == "\u20B9")
            0.0 else if (this.toString().toCharArray()[0] == '.')
            this.toString().replace(".", "0.").toDouble() else
            this.toString().replace("\u20B9", "").toDouble()
    } catch (ex: Exception) {
        0.0
    }
}

/*fun String?.textCapWords(): CharSequence? {
    val list = this
        ?.split(" ")
        ?.map { it.substring(0,1).toUpperCase() + it.substring(1) }
        .also { println(it) }
    return list?.joinToString(separator = " ")
}*/

fun String?.textCapWords(): CharSequence? {
    val list = this
        ?.split(" ")
        ?.map {
            if (it.isNotEmpty()) it.substring(
                0,
                1
            ).toUpperCase() + (if (it.length > 1) it.substring(1) else null) else it
        }
        .also { println(it) }
    return list?.joinToString(separator = " ")
}

fun Double.toString(decimal: Int): String {
    val builder = StringBuilder("#0.")
    for (i in 0 until decimal) builder.append("0")
    return DecimalFormat(builder.toString()).format(this)
}

fun Double.roundTo(decimals: Int): Double =
    Math.round(this * Math.pow(10.0, decimals.toDouble())) / Math.pow(10.0, decimals.toDouble())

fun Double.roundToTest(): Long =
    this.roundToLong()

fun Int.appendZeros(i: Int): String =
    String.format("%0${i}d", this)

fun Double.isNotZero(): Boolean =
    this != 0.0 && this != -0.0

fun Int.isNotZero(): Boolean =
    this != 0 && this != -0

fun Double.isZero(): Boolean =
    this == 0.0 || this == -0.0

fun Double.isPositive(): Boolean = this > 0.0
fun Double.isNegative(): Boolean = this < 0.0

val Double.Companion.ZERO: Double
    get() = 0.0

val Long.Companion.ZERO: Long
    get() = 0L

val String.Companion.EMPTY: String
    get() {
        return ""
    }

val String.Companion.SPACE: String
    get() {
        return " "
    }

val Char.Companion.ZERO: Char
    get() {
        return '0'
    }

val String.Companion.ZERO: String
    get() {
        return "0"
    }

fun Double.positive(): Double {
    // return value will be always +ve irrespective of input
    return if (this == 0.0) 0.0 else if (this >= 0.0) this else -1 * this
}

fun Long.positive(): Long {
    // return value will be always +ve irrespective of input
    return if (this == 0L) 0L else if (this >= 0L) this else -1 * this
}

fun Double.negative(): Double {
    // return value will be always -ve irrespective of input
    return if (this == 0.0) 0.0 else if (this < 0.0) this else -1 * this
}

fun Double.negation(): Double {
    return -1 * this
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun Array<String>.getAsArrayList(): ArrayList<String> {
    val list = ArrayList<String>()
    this.forEach { it->list.add(it) }
    return list
}
fun String.makeDirectory() {
    File(Environment.getExternalStorageDirectory().path, this)
        .also { file ->

            when {
                file.exists().not() -> {
                    try {
                        if (file.mkdirs().not()) {
                            println("$TAG_PRINT_INVOICE Unable to create the directory $file.")
                        }
                    } catch (e: Exception) {
                        println("$TAG_PRINT_INVOICE $e")
                    }
                }
                file.exists() && file.canWrite().not() -> {
                    println("$TAG_PRINT_INVOICE Required permissions denied")
                }
            }
        }
}