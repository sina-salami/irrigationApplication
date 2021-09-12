package ir.sinasalami.irrigation

import android.content.res.Resources
import org.locationtech.jts.geom.Coordinate
import kotlin.math.abs

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
val Float.toDp get() = this / Resources.getSystem().displayMetrics.density

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Coordinate.isBetween(previous: Coordinate, next: Coordinate): Boolean {
    val m = (next.y - previous.y) / (next.x - previous.x)
    return abs((y - previous.y) - (m * (x - previous.x))) < 0.00001
}