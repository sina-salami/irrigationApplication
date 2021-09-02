package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.withRotation
import androidx.core.view.setPadding
import org.locationtech.jts.geom.Coordinate
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
    var furrows = emptyList<Furrow>()
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
    var waterOutlet: Coordinate? = null
        set(value) {
            field = value
            refreshFieldRect()
            invalidate()
        }
    var slopeXY: Pair<Float, Float>? = null
        set(value) {
            field = value
            invalidate()
        }

    private val _tempPoint = PointF()
    private val fieldRect = RectD()
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

    private val waterOutletPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        alpha = 128
    }

    private val furrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLUE
        alpha = 64
        strokeWidth = 2f.toPx
    }
    private val onSurfaceFurrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLUE
        strokeWidth = 2f.toPx
    }

    private val slopeArrowPath = Path()
    private val slopeArrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.toPx
        color = Color.BLACK
    }
    private val slopeTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            10f,
            Resources.getSystem().displayMetrics
        )
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        drawField(canvas)
        drawWaterEntrance(canvas)
        drawWaterOutlet(canvas)
        drawFurrows(canvas)
        drawOnSurfaceFurrows(canvas)
        drawSlopeArrows(canvas)
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

    private fun drawWaterOutlet(canvas: Canvas) {
        val finalOutlet = waterOutlet ?: return
        val point = convert(finalOutlet, _tempPoint)

        canvas.drawCircle(point.x + offsetX(), point.y + offsetY(), 6f.toPx, waterOutletPaint)
    }

    private fun drawFurrows(canvas: Canvas) {
        for (furrow in furrows) {
            fieldPath.reset()

            for (coordinate in furrow.coordinates) {
                val point = convert(coordinate, _tempPoint)

                if (fieldPath.isEmpty)
                    fieldPath.moveTo(point.x, point.y)
                else
                    fieldPath.lineTo(point.x, point.y)
            }

            fieldPath.offset(offsetX(), offsetY())

            canvas.drawPath(fieldPath, furrowPaint)
        }
    }

    private fun drawOnSurfaceFurrows(canvas: Canvas) {
        for (furrow in furrows) {
            fieldPath.reset()

            var lastCoordinate: Coordinate? = null
            loop@ for (coordinate in furrow.coordinates) {
                val point = convert(coordinate, _tempPoint)

                when {
                    fieldPath.isEmpty -> fieldPath.moveTo(point.x, point.y)
                    furrow.onSurfaceHead != null &&
                            lastCoordinate != null &&
                            furrow.onSurfaceHead.isBetween(lastCoordinate, coordinate) -> {
                        val onSurfacePoint = convert(furrow.onSurfaceHead, _tempPoint)
                        fieldPath.lineTo(onSurfacePoint.x, onSurfacePoint.y)
                        break@loop
                    }
                    else ->
                        fieldPath.lineTo(point.x, point.y)

                }
                lastCoordinate = coordinate
            }
            fieldPath.offset(offsetX(), offsetY())

            canvas.drawPath(fieldPath, onSurfaceFurrowPaint)
        }
    }

    private fun drawSlopeArrows(canvas: Canvas) {
        val finalSlope = slopeXY ?: return

        val arrowSize = 10f.toPx
        val centerY = paddingTop.toFloat()
        val centerX = paddingLeft.toFloat() + arrowSize
        slopeArrowPath.moveTo(centerX, centerY)
        slopeArrowPath.rLineTo(-arrowSize / 2, arrowSize)
        slopeArrowPath.moveTo(centerX, centerY)
        slopeArrowPath.rLineTo(arrowSize / 2, arrowSize)
        slopeArrowPath.moveTo(centerX, centerY)
        slopeArrowPath.rLineTo(0f, arrowSize * 2)

        canvas.drawPath(slopeArrowPath, slopeArrowPaint)

        canvas.withRotation(90f, centerX, centerY + arrowSize * 2) {
            canvas.drawPath(slopeArrowPath, slopeArrowPaint)
        }

        val text = "${finalSlope.first},${finalSlope.second}"
        val textHeight = 16.toPx // shout use Paint#getTextBounds
        canvas.drawText(text, centerX, centerY + arrowSize * 2 + textHeight, slopeTextPaint)
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
        fieldRect.left = Double.MAX_VALUE
        fieldRect.top = Double.MAX_VALUE
        fieldRect.right = Double.MIN_VALUE
        fieldRect.bottom = Double.MIN_VALUE

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

        waterOutlet?.let {
            fieldRect.top = minOf(fieldRect.top, it.y)
            fieldRect.bottom = maxOf(fieldRect.bottom, it.y)
            fieldRect.left = minOf(fieldRect.left, it.x)
            fieldRect.right = maxOf(fieldRect.right, it.x)
        }

        for (furrow in furrows) {
            for (coordinate in furrow.coordinates) {
                fieldRect.top = minOf(fieldRect.top, coordinate.y)
                fieldRect.bottom = maxOf(fieldRect.bottom, coordinate.y)
                fieldRect.left = minOf(fieldRect.left, coordinate.x)
                fieldRect.right = maxOf(fieldRect.right, coordinate.x)
            }
        }
    }

    init {
        if (isInEditMode) {
            setPadding(48.toPx)
            fieldCoordinates = DataFactory.miladTowerCoordinates
            waterEntrance = DataFactory.miladEntrance
            waterOutlet = DataFactory.miladOutlet
            furrows = DataFactory.miladFurrow
            slopeXY = 22.3f to 45.3f
        }
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


    fun Coordinate.isBetween(previous: Coordinate, next: Coordinate): Boolean =
        x in minOf(previous.x, next.x)..maxOf(previous.x, next.x) &&
                y in minOf(previous.y, next.y)..maxOf(previous.y, next.y)


    data class Furrow(
        val coordinates: List<Coordinate>,
        val onSurfaceHead: Coordinate? = null
    )

    data class RectD(
        var left: Double = 0.0,
        var top: Double = 0.0,
        var right: Double = 0.0,
        var bottom: Double = 0.0
    )
}