package eo.view.signalstrength.util

import android.content.Context
import kotlin.math.max


internal fun Context.loadIntArrayAsRatioPoints(intArrayRes: Int): FloatArray {
    val points = resources.getIntArray(intArrayRes)
    var maxX = points[0]
    var maxY = points[1]

    for (index in 2..points.lastIndex step 2) {
        maxX = max(maxX, points[index])
        maxY = max(maxY, points[index + 1])
    }

    val ratioPoints = FloatArray(points.size)
    for (index in 0..ratioPoints.lastIndex step 2) {
        ratioPoints[index] = points[index].toFloat() / maxX
        ratioPoints[index + 1] = points[index + 1].toFloat() / maxY
    }

    return ratioPoints
}