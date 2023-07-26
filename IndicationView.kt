package com.wholesale.jewels.fauxiq.baheekhata.ui.components

import android.content.Context
import androidx.databinding.BindingAdapter
import android.graphics.*
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.wholesale.jewels.fauxiq.baheekhata.R

@BindingAdapter("src_color")
fun sourceColor(view: IndicationView, color: Int) {
    view.setColor(color)
}

class IndicationView : View {

    private val GRAVITY_TOP = 1
    private val GRAVITY_RIGHT = 2
    private val GRAVITY_TOP_RIGHT = 3
    private val GRAVITY_BOTTOM = 4
    private val GRAVITY_BOTTOM_RIGHT = 6
    private val GRAVITY_LEFT = 8
    private val GRAVITY_TOP_LEFT = 9
    private val GRAVITY_BOTTOM_LEFT = 12

    private var color: Int = Color.RED
    private var lineWidth: Int = 0
    private var p: Paint? = null
    private var gravity: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet) {

        // real work here
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.IndicationView,
            0, 0
        )

        try {

            lineWidth = a.getDimension(R.styleable.IndicationView_line_width, 10f).toInt()
            gravity = a.getInt(R.styleable.IndicationView_gravity, 0)
        } finally {
            a.recycle()
        }

        p = Paint()
        p!!.color = color
    }

    fun setColor(color: Int){
        this.color = color
        p!!.color = color
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD

        lateinit var l1: Point
        lateinit var l2: Point
        lateinit var l3: Point
        lateinit var l4: Point

        lateinit var b1: Point
        lateinit var b2: Point
        lateinit var b3: Point

        when (gravity) {
            GRAVITY_TOP, GRAVITY_RIGHT, GRAVITY_TOP_RIGHT -> {

                l1 = Point(0, 0)
                l2 = Point(width, height)
                l3 = Point(width, (height - lineWidth))
                l4 = Point(lineWidth, 0)

                b1 = Point(lineWidth + lineWidth, 0)
                b2 = Point(width, height - lineWidth - lineWidth)
                b3 = Point(width, 0)
            }
            GRAVITY_LEFT, GRAVITY_TOP_LEFT -> {

                l1 = Point((width - lineWidth), 0)
                l2 = Point(width, 0)
                l3 = Point(0, height)
                l4 = Point(0, (height - lineWidth))

                b1 = Point(0, 0)
                b2 = Point((width - lineWidth - lineWidth), 0)
                b3 = Point(0, (height - lineWidth - lineWidth))
            }
            GRAVITY_BOTTOM, GRAVITY_BOTTOM_RIGHT -> {

                l1 = Point(width, 0)
                l2 = Point(width, lineWidth)
                l3 = Point(lineWidth, height)
                l4 = Point(0, height)

                b1 = Point(width, height)
                b2 = Point(width, lineWidth+lineWidth)
                b3 = Point(lineWidth+lineWidth, height)
            }
            GRAVITY_BOTTOM_LEFT -> {

                l1 = Point(0,0)
                l2 = Point(width, height)
                l3 = Point((width - lineWidth), height)
                l4 = Point(0, lineWidth)

                b1 = Point(0, height)
                b2 = Point((width - lineWidth - lineWidth), height)
                b3 = Point(0, lineWidth+lineWidth)
            }
        }

        //        Point l1 = new Point();


        path.moveTo(l1.x.toFloat(), l1.y.toFloat())
        path.lineTo(l2.x.toFloat(), l2.y.toFloat())
        path.lineTo(l3.x.toFloat(), l3.y.toFloat())
        path.lineTo(l4.x.toFloat(), l4.y.toFloat())
        path.close()

        canvas.drawPath(path, p!!)

        path.moveTo(b1.x.toFloat(), b1.y.toFloat())
        path.lineTo(b2.x.toFloat(), b2.y.toFloat())
        path.lineTo(b3.x.toFloat(), b3.y.toFloat())
        path.close()

        canvas.drawPath(path, p!!)
    }
}
