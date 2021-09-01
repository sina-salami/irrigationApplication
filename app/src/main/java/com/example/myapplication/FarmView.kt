package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs

class FarmView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var fieldCoordinates = emptyList<Coordinate>()
        set(value) {
            field = value
            refreshFieldRect()
            invalidate()
        }
    var waterEntrance: Coordinate? = null
        set(value) {
            field = value
            refreshFieldRect()
            invalidate()
        }

    private val _tempPoint = PointF()
    private val fieldRect =
        RectD(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE)
    private val fieldPath = Path()
    private val fieldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.RED
        alpha = 128
        strokeWidth = 2f.toPx
    }


    private val waterEntrancePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
        alpha = 128
    }

    override fun onDraw(canvas: Canvas) {
        drawField(canvas)
        drawWaterEntrance(canvas)
    }

    private fun drawField(canvas: Canvas) {
        fieldPath.reset()
        fieldCoordinates.forEach {
            val point = convert(it, _tempPoint)

            if (fieldPath.isEmpty)
                fieldPath.moveTo(point.x, point.y)
            else
                fieldPath.lineTo(point.x, point.y)
        }
        fieldPath.close()

        fieldPath.offset(offsetX(), offsetY())

        canvas.drawPath(fieldPath, fieldPaint)
    }

    private fun drawWaterEntrance(canvas: Canvas) {
        val finalEntrance = waterEntrance ?: return
        val point = convert(finalEntrance, _tempPoint)

        canvas.drawCircle(point.x + offsetX(), point.y + offsetY(), 10f.toPx, waterEntrancePaint)
    }

    private fun offsetX(): Float {
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val multipleY = height / (fieldRect.bottom - fieldRect.top)
        val multipleX = width / (fieldRect.right - fieldRect.left)
        val multiple = minOf(multipleX, multipleY)

        return (abs(width - ((fieldRect.right - fieldRect.left) * multiple)) / 2f).toFloat()
    }

    private fun offsetY(): Float {
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val multipleY = height / (fieldRect.bottom - fieldRect.top)
        val multipleX = width / (fieldRect.right - fieldRect.left)
        val multiple = minOf(multipleX, multipleY)

        return (abs(height - ((fieldRect.bottom - fieldRect.top) * multiple)) / 2f).toFloat()
    }

    private fun refreshFieldRect() {
        for (coordinate in fieldCoordinates) {
            fieldRect.top = minOf(fieldRect.top, coordinate.y)
            fieldRect.bottom = maxOf(fieldRect.bottom, coordinate.y)
            fieldRect.left = minOf(fieldRect.left, coordinate.x)
            fieldRect.right = maxOf(fieldRect.right, coordinate.x)
        }

        waterEntrance?.let {
            fieldRect.top = minOf(fieldRect.top, it.y)
            fieldRect.bottom = maxOf(fieldRect.bottom, it.y)
            fieldRect.left = minOf(fieldRect.left, it.x)
            fieldRect.right = maxOf(fieldRect.right, it.x)
        }
    }

    init {
        if (isInEditMode)
            fieldCoordinates = listOf(
                Coordinate(51.3673715262361, 35.74505358990784),
                Coordinate(51.37027891200881, 35.73837003960733),
                Coordinate(51.3755134676994, 35.73776272335922),
                Coordinate(51.3798550876044, 35.74130467651047),
                Coordinate(51.37554714944172, 35.74552452283459),
                Coordinate(51.375614240000, 35.74124283459),
            )
        waterEntrance = Coordinate(51.3803715262361, 35.73905358990784)
    }

    private fun convert(coordinate: Coordinate, outPoint: PointF): PointF {
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val multipleY = height / (fieldRect.bottom - fieldRect.top)
        val multipleX = width / (fieldRect.right - fieldRect.left)
        val multiple = minOf(multipleX, multipleY)

        outPoint.set(
            ((coordinate.x - fieldRect.left) * multiple).toFloat() + paddingLeft,
            ((coordinate.y - fieldRect.top) * multiple).toFloat() + paddingTop
        )
        return outPoint
    }

    data class Coordinate(val x: Double, val y: Double)
    data class RectD(
        var left: Double = 0.0,
        var top: Double = 0.0,
        var right: Double = 0.0,
        var bottom: Double = 0.0
    )
}