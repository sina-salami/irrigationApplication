package ir.sinasalami.irrigation

import android.content.res.Resources
import org.locationtech.jts.geom.Coordinate

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
val Float.toDp get() = this / Resources.getSystem().displayMetrics.density

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()


fun Coordinate.isBetween(previous: Coordinate, next: Coordinate): Boolean =
    x in minOf(previous.x, next.x)..maxOf(previous.x, next.x) &&
            y in minOf(previous.y, next.y)..maxOf(previous.y, next.y)