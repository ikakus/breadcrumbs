package com.ikakus.breadcrumbs.view.days

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout


class DaysView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var dayWidth: Float = 0f
    private val TAG = "DaysView"
    private val totalDays = 33
    var daysCommited = 33

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        Log.d("$TAG onMeasure", widthSize.toString())

        val desiredHeight = measuredWidth / totalDays
        dayWidth = (measuredWidth / totalDays).toFloat()

        setMeasuredDimension(width, desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawDays(canvas)
    }

    var paint: Paint = Paint()

    private fun drawDays(canvas: Canvas) {
        paint.run {
            color = Color.BLACK
            style = Paint.Style.FILL
        }
        for (i in daysCommited..totalDays) {
            canvas.drawRect(
                i * (dayWidth),
                0f,
                dayWidth + i * dayWidth,
                dayWidth,
                paint
        )
        }

        paint.run {
            color = Color.YELLOW
            style = Paint.Style.STROKE
        }
        for (i in 0 .. daysCommited) {
            canvas.drawRect(
                i * (dayWidth),
                0f,
                dayWidth + i * dayWidth,
                dayWidth,
                paint
            )
        }

    }
}