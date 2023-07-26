package com.wholesale.jewels.fauxiq.baheekhata.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View.OnTouchListener


class PasswordView : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        this.setOnTouchListener(onTouchListener)
        this.transformationMethod = PasswordTransformationMethod()
        this.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        this.maxLines = 1
    }

    private fun getPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()

    private fun getPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private val onTouchListener = OnTouchListener { _, event ->
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (this@PasswordView.width - event.x < getPx(40)) {
                this.transformationMethod = null
                this.setSelection(this.text?.length ?: 0)
                return@OnTouchListener true
            }
        } else if (event?.action == MotionEvent.ACTION_UP) {
            if (this@PasswordView.width - event.x < getPx(40)) {
                this.transformationMethod = PasswordTransformationMethod()
                this.setSelection(this.text?.length ?: 0)
                return@OnTouchListener true
            }
        }
        false
    }
}
