package com.wholesale.jewels.fauxiq.baheekhata.utils

import androidx.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.Color
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.wholesale.jewels.fauxiq.baheekhata.ui.components.SearchView
import com.wholesale.jewels.fauxiq.baheekhata.ui.components.SearchViewWithFilter
import java.util.*

@BindingAdapter("android:isSelected")
fun setSelected(view: TextView, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("android:imageBitmap")
fun imageBitmap(view: ImageView, imageBitmap: Bitmap?) {
    view.setImageBitmap(imageBitmap)

    if (imageBitmap == null)
        view.setImageResource(com.wholesale.jewels.fauxiq.baheekhata.R.drawable.placeholder)
}

@BindingAdapter("android:paddingTop")
fun imageBitmap(view: ViewGroup, dp: Int) {
    view.setPadding( 0, Utils.getPx(view.context, dp), 0, 0)
    view.invalidate()
}

@BindingAdapter("android:imageText")
fun imageText(view: TextView, imageText: String?) {
    if(imageText != null){
        view.text = imageText.toCharArray()[0].toString().toUpperCase(Locale.ROOT)
    }
//    view.text = imageText.toCharArray()[0].toString().toUpperCase()
}

@BindingAdapter("android:spannableText")
fun spannableText(view: TextView, spannableText: String) {

    var spannableTextIndex = view.text.toString().indexOf(spannableText)
    if (spannableTextIndex == -1) spannableTextIndex = 0

    val spannable = SpannableString(view.text)

    spannable.setSpan(
        ForegroundColorSpan(Color.parseColor("#1976D2")),
        spannableTextIndex,
        spannableTextIndex + spannableText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    view.setText(spannable, TextView.BufferType.SPANNABLE)
}

@BindingAdapter("android:spannableText", "android:spannableTextIndex")
fun spannableTextWithIndex(view: TextView, spannableText: String, spannableTextIndex:Int) {

    var index = view.text.toString().indexOf(spannableText, spannableTextIndex)
    if (index==-1) index = 0
    val spannable = SpannableString(view.text)

    spannable.setSpan(
        ForegroundColorSpan(Color.parseColor("#1976D2")),
        index,
        index + spannableText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    view.setText(spannable, TextView.BufferType.SPANNABLE)
}

@BindingAdapter("android:spannableText", "android:spannableTextColor")
fun spannableTextWithColor(view: TextView, spannableText: String, color:Int) {

    var index = view.text.toString().indexOf(spannableText)
    if (index==-1) index = 0
    val spannable = SpannableString(view.text)

    spannable.setSpan(
        ForegroundColorSpan(color),
        index,
        index + spannableText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    view.setText(spannable, TextView.BufferType.SPANNABLE)
}

@BindingAdapter("android:OnSearchButtonClickListener")
fun OnSearchButtonClickListener(searchView: SearchView, onSearchButtonClickListener: View.OnClickListener){
    searchView.setOnSearchButtonClickListener(onSearchButtonClickListener)
}

@BindingAdapter("android:OnSearchButtonClickListener")
fun OnSearchButtonClickListener(searchView: SearchViewWithFilter, onSearchButtonClickListener: View.OnClickListener){
    searchView.setOnSearchButtonClickListener(onSearchButtonClickListener)
}

@BindingAdapter("android:DecimalDigitsBeforeZero")
fun DecimalDigitsAfterZero(view: EditText, decimal: Int){
    view.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(decimal, 2))
}

@BindingAdapter("android:DecimalDigitsAfterZero")
fun DecimalDigitsBeforeZero(view: EditText, decimal: Int){
    view.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter( 10, decimal))
}

@BindingAdapter("android:DecimalDigitsBeforeZero", "android:DecimalDigitsAfterZero")
fun DecimalDigitsBothDigits(view: EditText, decimalBefore: Int, decimalAfter: Int){
    view.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter( decimalBefore, decimalAfter))
}