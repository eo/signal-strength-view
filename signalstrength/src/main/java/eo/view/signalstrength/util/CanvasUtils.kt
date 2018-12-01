package eo.view.signalstrength.util

import android.graphics.Canvas

/**
 * Wrap the specified [block] in calls to [Canvas.save]
 * and [Canvas.restoreToCount].
 */
inline fun Canvas.withSave(block: Canvas.() -> Unit) {
    val checkpoint = save()
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}