package ir.sinasalami.irrigation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
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
    private val isDebug = false

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
    var waterHeight: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val _tempRectF = RectF()
    private val _tempPath = Path()

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

    private val waterPath = Path()
    private val waterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f.toPx
        color = Color.BLUE
    }


    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f.toPx
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        drawBedPath(canvas)

        drawWater(canvas)
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

        // close bed path to be able intersect bed a water paths
        bedPath.lineTo(endPoint.x, startPoint.y)
        bedPath.close()
    }

    private fun drawWater(canvas: Canvas) {
        val water = waterHeight.toPx
        val h = bedHeight.toPx

        bedPath.computeBounds(_tempRectF, true)
        val width = _tempRectF.width()

        waterPath.reset()
        waterPath.moveTo(0f, h - water)
        waterPath.rLineTo(width, 0f)
        waterPath.rLineTo(0f, water)
        waterPath.rLineTo(-width, 0f)
        waterPath.close()

        _tempPath.reset()
        _tempPath.op(waterPath, bedPath, Path.Op.INTERSECT)
        canvas.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
            canvas.drawPath(_tempPath, waterPaint)
            canvas.withScale(x = -1f, pivotX = endPoint.x) {
                drawPath(_tempPath, waterPaint)
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
            waterHeight = 50f
        }
    }
}