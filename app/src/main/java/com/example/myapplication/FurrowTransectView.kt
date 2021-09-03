package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import androidx.core.view.setPadding

class FurrowTransectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val isDebug = true

    var highWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var middleWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var lowWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var bedHeight: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val startPoint = PointF()
    private val point1 = PointF()
    private val point2 = PointF()
    private val endPoint = PointF()
    private val bedPath = Path()
    private val bedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f.toPx
        color = Color.YELLOW
    }

    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f.toPx
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        drawBedPath(canvas)
    }

    private fun drawBedPath(canvas: Canvas) {
        val bedExtraLength = 16f.toPx

        val h = bedHeight.toPx
        val high = highWidth.toPx + bedExtraLength
        val mid = middleWidth.toPx + bedExtraLength
        val low = lowWidth.toPx + bedExtraLength

        startPoint.set(0f, 0f)
        point1.set(high - mid, h * 0.4f)
        point2.set(high - low, h * 0.95f)
        endPoint.set(high, h)

        bedPath.reset()
        bedPath.rLineTo(bedExtraLength, 0f)
        bedPath.cubicTo(point1.x, point1.y, point2.x, point2.y, endPoint.x, endPoint.y)

        canvas.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
            canvas.drawPath(bedPath, bedPaint)
            canvas.withScale(x = -1f, pivotX = endPoint.x) {
                drawPath(bedPath, bedPaint)
            }

            if (isDebug) {
                canvas.drawCircle(startPoint.x, startPoint.y, 4f.toPx, debugPaint)
                canvas.drawCircle(point1.x, point1.y, 4f.toPx, debugPaint)
                canvas.drawCircle(point2.x, point2.y, 4f.toPx, debugPaint)
                canvas.drawCircle(endPoint.x, endPoint.y, 4f.toPx, debugPaint)
            }
        }
    }

    init {
        if (isInEditMode) {
            setPadding(20.toPx)
            highWidth = 100f
            middleWidth = 70f
            lowWidth = 20f
            bedHeight = 100f
        }
    }
}