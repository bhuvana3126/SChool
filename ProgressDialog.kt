package com.wholesale.jewels.fauxiq.baheekhata.ui.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Window
import android.widget.ProgressBar
import android.widget.RelativeLayout

class ProgressDialog(context: Context) : Dialog(context) {

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(getView(context))
        this.window!!.setBackgroundDrawable(getBackgroundDrawable(context))
        this.setCancelable(false)
        this.setTitle("")
    }

    private fun getView(context: Context): RelativeLayout {

        val relativeLayout = RelativeLayout(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            relativeLayout.background = getBackgroundDrawable(context)
        } else {
            relativeLayout.setBackgroundDrawable(getBackgroundDrawable(context))
        }
        relativeLayout.setPadding(
                getPx(context, 8),
                getPx(context, 8),
                getPx(context, 8),
                getPx(context, 8))

        val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        relativeLayout.addView(progressBar)
        val layoutParams = progressBar.layoutParams as RelativeLayout.LayoutParams
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        progressBar.layoutParams = layoutParams

        return relativeLayout
    }

    private fun getBackgroundDrawable(context: Context): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(Color.WHITE)
        shape.setStroke(3, Color.parseColor("#5a5a5a5a"))
        shape.cornerRadius = getPx(context, 8).toFloat()
        return shape
    }

    private fun getPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics).toInt()
    }

    override fun setCancelable(b: Boolean) {
        super.setCancelable(b)
    }
}