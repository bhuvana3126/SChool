package com.wholesale.jewels.fauxiq.baheekhata.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.utils.ResourceUtil


class SearchViewWithFilter : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private lateinit var mPaint: Paint
    private lateinit var mResources: Resources
    private lateinit var mBitmap: Bitmap

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        this.isLongClickable = false

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mResources = context.resources

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBitmap = ResourceUtil.getBitmap(context, R.drawable.ic_search_accent)
        } else {
            throw RuntimeException("Unable to convert .svg to Bitmap")
        }

        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_filter, 0)
        this.hint = "Search"
        this.setPadding(
            getPx(context, 16),
            getPx(context, 16),
            getPx(context, 16),
            getPx(context, 16)
        )
        this.imeOptions = EditorInfo.IME_ACTION_SEARCH
        this.inputType = EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES
        this.setTextColor(Color.parseColor("#5a5a5a"))
        this.setHintTextColor(Color.parseColor("#5c5a5a5a"))
        this.setBackgroundResource(R.drawable.search_view_bg)

        /*val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        f.isAccessible = true
        f.set(this, R.drawable.color_cursor)*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = getPx(context, 4).toFloat()
        }

        this.setOnTouchListener(onTouchListener)

        this.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (onSearchButtonClickListener != null) {
                    this@SearchViewWithFilter.clearFocus()
                    hideKeyboard(context = context)
                    onSearchButtonClickListener!!.onClick(this)
                    if (handler != null) handler.removeCallbacksAndMessages(null)
                }
            }
            true
        }

        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                handler?.postDelayed({
                    if (onTextChangeListener != null) {
                        onTextChangeListener!!.onClick(this@SearchViewWithFilter)
                    }
                }, 600)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (handler != null) handler.removeCallbacksAndMessages(null)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(mBitmap, (width - getPx(2 * 48)).toFloat(), getPx(16).toFloat(), mPaint)
        mPaint.color = Color.parseColor("#1976D2")
        mPaint.strokeWidth = getPx(2).toFloat()
        canvas?.drawLine(
            (width - getPx(56)).toFloat(),
            getPx(16).toFloat(),
            (width - getPx(56)).toFloat(),
            getPx(40).toFloat(), mPaint
        )
        super.onDraw(canvas)

    }

    private fun getPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()

    private var onSearchButtonClickListener: View.OnClickListener? = null
    private var onFilterButtonClickListener: View.OnClickListener? = null
    private var onTextChangeListener: OnClickListener? = null

    fun setOnSearchButtonClickListener(onClearButtonClickListener: View.OnClickListener) {
        this.onSearchButtonClickListener = onClearButtonClickListener
    }

    fun setOnFilterButtonClickListener(onClearButtonClickListener: View.OnClickListener) {
        this.onFilterButtonClickListener = onClearButtonClickListener
    }

    fun setOnTextChangeListener(onClickListener: OnClickListener) {
        this.onTextChangeListener = onClickListener
    }

    private fun getPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private val onTouchListener = OnTouchListener { _, event ->
        if (event?.action == MotionEvent.ACTION_UP) {

            if (isSearchButtonClicked(event)) {
                if (onSearchButtonClickListener != null) {
                    hideKeyboard(context = context)
                    this@SearchViewWithFilter.clearFocus()
                    onSearchButtonClickListener!!.onClick(this)
                }
                return@OnTouchListener true

            } else if (isFilterButtonClicked(event)) {

                if (onFilterButtonClickListener != null) {
                    hideKeyboard(context = context)
                    this@SearchViewWithFilter.clearFocus()
                    onFilterButtonClickListener!!.onClick(this)
                }
                return@OnTouchListener true
            }
        }
        false
    }

    private fun isSearchButtonClicked(event: MotionEvent): Boolean {
        return this@SearchViewWithFilter.width - event.x < getPx(96) && this@SearchViewWithFilter.width - event.x > getPx(
            48
        )
    }

    private fun isFilterButtonClicked(event: MotionEvent): Boolean =
        this@SearchViewWithFilter.width - event.x < getPx(48)

    fun hideKeyboard(context: Context) {

        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        val token = this.windowToken
        imm.hideSoftInputFromWindow(token, 0)
    }

    fun performSearchButtonClick() {

        if (onSearchButtonClickListener != null) {
            this@SearchViewWithFilter.clearFocus()
            hideKeyboard(context = context)
            onSearchButtonClickListener!!.onClick(this)
        }
    }

    fun onDetach() {
        if (handler != null) handler.removeCallbacksAndMessages(null)
    }

    fun clearText() {
        super.setText(null)
        if (handler != null) handler.removeCallbacksAndMessages(null)
        performSearchButtonClick()
    }
}
