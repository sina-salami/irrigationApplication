package ir.sinasalami.irrigation

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
import org.locationtech.jts.algorithm.Length
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.linearref.LengthIndexedLine
import kotlin.math.abs

class FarmView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var fieldCoordinates = emptyList<Coordinate>()
        set(value) {
            field = value
            refreshBounding()
            invalidate()
        }
    var furrows = emptyList<Furrow>()
        set(value) {
            field = value
            refreshBounding()
            invalidate()
        }
    var onSurfacePercent: Int? = null
        set(value) {
            field = value
            invalidate()
        }
    var underSurfacePercent: Int? = null
        set(value) {
            field = value
            invalidate()
        }
    var waterEntrance: Coordinate? = null
        set(value) {
            field = value
            refreshBounding()
            invalidate()
        }
    var waterOutlet: Coordinate? = null
        set(value) {
            field = value
            refreshBounding()
            invalidate()
        }
    var slopeXY: Pair<Float, Float>? = null
        set(value) {
            field = value
            invalidate()
        }

    private val _tempPoint = PointF()
    private val _tempPath = Path()

    private val fieldRect = RectD()
    private val fieldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.RED
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
    private val underSurfaceFurrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.MAGENTA
        strokeWidth = 10f.toPx
        alpha = 128
    }

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
        drawUnderSurfaceFurrows(canvas)
        drawFurrows(canvas)
        drawOnSurfaceFurrows(canvas)
        drawSlopeArrows(canvas)
    }

    private fun drawField(canvas: Canvas) {
        _tempPath.reset()
        fieldCoordinates.forEach {
            val point = convert(it, _tempPoint)

            if (_tempPath.isEmpty)
                _tempPath.moveTo(point.x, point.y)
            else
                _tempPath.lineTo(point.x, point.y)
        }
        _tempPath.close()

        _tempPath.offset(offsetX(), offsetY())

        canvas.drawPath(_tempPath, fieldPaint)
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
            _tempPath.reset()

            for (coordinate in furrow.coordinates) {
                val point = convert(coordinate, _tempPoint)

                if (_tempPath.isEmpty)
                    _tempPath.moveTo(point.x, point.y)
                else
                    _tempPath.lineTo(point.x, point.y)
            }

            _tempPath.offset(offsetX(), offsetY())

            canvas.drawPath(_tempPath, furrowPaint)
        }
    }

    private fun drawOnSurfaceFurrows(canvas: Canvas) {
        val finalPercent = onSurfacePercent ?: return

        for (furrow in furrows) {
            _tempPath.reset()

            val totalLength = furrow.calculateTotalLength()
            val headLength = totalLength * finalPercent / 100
            val headCoordinate = furrow.calculateCoordinateOf(headLength)

            var lastCoordinate: Coordinate? = null
            loop@ for (coordinate in furrow.coordinates) {
                val point = convert(coordinate, _tempPoint)

                when {
                    _tempPath.isEmpty -> _tempPath.moveTo(point.x, point.y)
                    headCoordinate == null -> break@loop
                    lastCoordinate != null &&
                        headCoordinate.isBetween(lastCoordinate, coordinate) -> {
                        val onSurfacePoint = convert(headCoordinate, _tempPoint)
                        _tempPath.lineTo(onSurfacePoint.x, onSurfacePoint.y)
                        break@loop
                    }
                    else ->
                        _tempPath.lineTo(point.x, point.y)

                }
                lastCoordinate = coordinate
            }
            _tempPath.offset(offsetX(), offsetY())

            canvas.drawPath(_tempPath, onSurfaceFurrowPaint)
        }
    }

    private fun drawUnderSurfaceFurrows(canvas: Canvas) {
        val finalPercent = underSurfacePercent ?: return

        for (furrow in furrows) {
            _tempPath.reset()

            val totalLength = furrow.calculateTotalLength()
            val headLength = totalLength * finalPercent / 100
            val headCoordinate = furrow.calculateCoordinateOf(headLength)

            var lastCoordinate: Coordinate? = null
            loop@ for (coordinate in furrow.coordinates) {
                val point = convert(coordinate, _tempPoint)

                when {
                    _tempPath.isEmpty -> _tempPath.moveTo(point.x, point.y)
                    headCoordinate == null -> break@loop
                    lastCoordinate != null &&
                        headCoordinate.isBetween(lastCoordinate, coordinate) -> {
                        val underSurfacePoint = convert(headCoordinate, _tempPoint)
                        _tempPath.lineTo(underSurfacePoint.x, underSurfacePoint.y)
                        break@loop
                    }
                    else ->
                        _tempPath.lineTo(point.x, point.y)

                }
                lastCoordinate = coordinate
            }
            _tempPath.offset(offsetX(), offsetY())

            canvas.drawPath(_tempPath, underSurfaceFurrowPaint)
        }
    }

    private fun drawSlopeArrows(canvas: Canvas) {
        val finalSlope = slopeXY ?: return

        val arrowSize = 10f.toPx
        val centerY = paddingTop.toFloat()
        val centerX = paddingLeft.toFloat() + arrowSize
        _tempPath.reset()
        _tempPath.moveTo(centerX, centerY)
        _tempPath.rLineTo(-arrowSize / 2, arrowSize)
        _tempPath.moveTo(centerX, centerY)
        _tempPath.rLineTo(arrowSize / 2, arrowSize)
        _tempPath.moveTo(centerX, centerY)
        _tempPath.rLineTo(0f, arrowSize * 2)

        canvas.drawPath(_tempPath, slopeArrowPaint)

        canvas.withRotation(90f, centerX, centerY + arrowSize * 2) {
            canvas.drawPath(_tempPath, slopeArrowPaint)
        }

        val text = "${finalSlope.first}, ${finalSlope.second}"
        val textHeight = 16.toPx // shout use Paint#getTextBounds
        canvas.drawText(text, centerX, centerY + arrowSize * 2 + textHeight, slopeTextPaint)
    }

    private fun offsetX(): Float {
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val multipleY = height / fieldRect.height
        val multipleX = width / fieldRect.width
        val multiple = minOf(multipleX, multipleY)

        return (abs(width - (fieldRect.width * multiple)) / 2f).toFloat()
    }

    private fun offsetY(): Float {
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val multipleY = height / fieldRect.height
        val multipleX = width / fieldRect.width
        val multiple = minOf(multipleX, multipleY)

        return (abs(height - (fieldRect.height * multiple)) / 2f).toFloat()
    }

    private fun refreshBounding() {
        fieldRect.left = Double.POSITIVE_INFINITY
        fieldRect.top = Double.NEGATIVE_INFINITY
        fieldRect.right = Double.NEGATIVE_INFINITY
        fieldRect.bottom = Double.POSITIVE_INFINITY

        for (coordinate in fieldCoordinates) {
            fieldRect.top = maxOf(fieldRect.top, coordinate.y)
            fieldRect.bottom = minOf(fieldRect.bottom, coordinate.y)
            fieldRect.left = minOf(fieldRect.left, coordinate.x)
            fieldRect.right = maxOf(fieldRect.right, coordinate.x)
        }

        waterEntrance?.let { coordinate ->
            fieldRect.top = maxOf(fieldRect.top, coordinate.y)
            fieldRect.bottom = minOf(fieldRect.bottom, coordinate.y)
            fieldRect.left = minOf(fieldRect.left, coordinate.x)
            fieldRect.right = maxOf(fieldRect.right, coordinate.x)
        }

        waterOutlet?.let { coordinate ->
            fieldRect.top = maxOf(fieldRect.top, coordinate.y)
            fieldRect.bottom = minOf(fieldRect.bottom, coordinate.y)
            fieldRect.left = minOf(fieldRect.left, coordinate.x)
            fieldRect.right = maxOf(fieldRect.right, coordinate.x)
        }

        for (furrow in furrows) {
            for (coordinate in furrow.coordinates) {
                fieldRect.top = maxOf(fieldRect.top, coordinate.y)
                fieldRect.bottom = minOf(fieldRect.bottom, coordinate.y)
                fieldRect.left = minOf(fieldRect.left, coordinate.x)
                fieldRect.right = maxOf(fieldRect.right, coordinate.x)
            }
        }
    }

    private fun convert(coordinate: Coordinate, outPoint: PointF): PointF {
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val multipleY = height / fieldRect.height
        val multipleX = width / fieldRect.width
        val multiple = minOf(multipleX, multipleY)

        outPoint.set(
            ((coordinate.x - fieldRect.left) * multiple).toFloat() + paddingLeft,
            ((-(coordinate.y - fieldRect.top)) * multiple).toFloat() + paddingTop
        )
        return outPoint
    }

    data class Furrow(val coordinates: List<Coordinate>) {
        fun calculateTotalLength(): Double {
            return Length.ofLine(GeometryFactory().coordinateSequenceFactory.create(coordinates.toTypedArray()))
        }

        fun calculateCoordinateOf(length: Double): Coordinate? {
            val lineString =
                LengthIndexedLine(GeometryFactory().createLineString(coordinates.toTypedArray()))
            if (lineString.isValidIndex(length).not()) return null

            val extractPoint = lineString.extractPoint(length)
            return Coordinate(extractPoint.x, extractPoint.y)
        }
    }

    data class RectD(
        var left: Double = 0.0,
        var top: Double = 0.0,
        var right: Double = 0.0,
        var bottom: Double = 0.0
    ) {
        val height: Double get() = abs(bottom - top)
        val width: Double get() = abs(right - left)
    }
}