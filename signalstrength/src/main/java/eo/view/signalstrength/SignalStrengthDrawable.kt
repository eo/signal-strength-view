package eo.view.signalstrength

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import eo.view.signalstrength.util.colorWithAlpha
import eo.view.signalstrength.util.getColorAttr
import eo.view.signalstrength.util.loadIntArrayAsRatioPoints
import eo.view.signalstrength.util.withSave


class SignalStrengthDrawable @JvmOverloads constructor(
    private val context: Context,
    theme: SignalStrength.Theme = SignalStrength.Theme.SHARP
) : Drawable(), SignalStrength {

    companion object {
        const val MINIMUM_SIGNAL_LEVEL = 0
        const val MAXIMUM_SIGNAL_LEVEL = 100

        private const val BACKGROUND_COLOR_ALPHA = 0.3f
        private const val ROUNDED_SHAPE_START_OFFSET = 0.07f
    }

    private val intrinsicSize =
        context.resources.getDimensionPixelSize(R.dimen.signal_strength_intrinsic_size)

    private val aspectRatio =
        context.resources.getFraction(R.fraction.signal_strength_aspect_ratio, 1, 1)

    private val padding = Rect()

    private var shapePoints: FloatArray = getShapePointsForTheme(theme)
    private val shapeBounds = Rect()
    private val shapePath = Path()
    private val signalLevelClipRect = RectF()

    private val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColorAttr(android.R.attr.colorForeground)
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = foregroundPaint.color.colorWithAlpha(BACKGROUND_COLOR_ALPHA)
    }

    override var theme: SignalStrength.Theme = theme
        set(value) {
            if (field != value) {
                field = value
                shapePoints = getShapePointsForTheme(value)
                updateShapePath()
                updateSignalLevelClipRect()
                invalidateSelf()
            }
        }

    override var signalLevel: Int = 0
        set(value) {
            val newSignalLevel = value.coerceIn(MINIMUM_SIGNAL_LEVEL, MAXIMUM_SIGNAL_LEVEL)
            if (field != newSignalLevel) {
                field = newSignalLevel
                updateSignalLevelClipRect()
                invalidateSelf()
            }
        }

    override var color: Int
        get() = foregroundPaint.color
        set(value) {
            if (color != value) {
                foregroundPaint.color = value
                backgroundPaint.color = value.colorWithAlpha(BACKGROUND_COLOR_ALPHA)
                invalidateSelf()
            }
        }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(shapePath, backgroundPaint)

        canvas.withSave {
            clipRect(signalLevelClipRect)
            drawPath(shapePath, foregroundPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        backgroundPaint.colorFilter = colorFilter
        foregroundPaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth() = if (aspectRatio < 1) {
        (intrinsicSize * aspectRatio).toInt()
    } else {
        intrinsicSize
    } + padding.left + padding.right

    override fun getIntrinsicHeight() = if (aspectRatio < 1) {
        intrinsicSize
    } else {
        (intrinsicSize / aspectRatio).toInt()
    } + padding.top + padding.bottom

    override fun getPadding(padding: Rect): Boolean {
        if (padding.left == 0 && padding.top == 0 && padding.right == 0 && padding.bottom == 0) {
            return super.getPadding(padding)
        }

        padding.set(this.padding)
        return true
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        padding.set(left, top, right, bottom)
        updateShapeBounds()
    }

    override fun onBoundsChange(bounds: Rect) {
        updateShapeBounds()
    }

    private fun updateShapeBounds() {
        if (bounds.isEmpty) return

        val availableWidth = bounds.width() - padding.left - padding.right
        val availableHeight = bounds.height() - padding.top - padding.bottom
        val availableAspectRatio = availableWidth.toFloat() / availableHeight

        if (availableAspectRatio > aspectRatio) {
            shapeBounds.set(0, 0, (availableHeight * aspectRatio).toInt(), availableHeight)
        } else {
            shapeBounds.set(0, 0, availableWidth, (availableWidth / aspectRatio).toInt())
        }

        shapeBounds.offset(
            padding.left + (availableWidth - shapeBounds.width()) / 2,
            padding.top + (availableHeight - shapeBounds.height()) / 2
        )

        updateShapePath()
        updateSignalLevelClipRect()
    }

    private fun updateShapePath() {
        with(shapePath) {
            reset()

            moveTo(
                xRatioToCoordinate(shapePoints[0]),
                yRatioToCoordinate(shapePoints[1])
            )

            if (theme == SignalStrength.Theme.SHARP) {
                for (index in 2..shapePoints.lastIndex step 2) {
                    lineTo(
                        xRatioToCoordinate(shapePoints[index]),
                        yRatioToCoordinate(shapePoints[index + 1])
                    )
                }
            } else {
                for (index in 2..shapePoints.lastIndex step 8) {
                    lineTo(
                        xRatioToCoordinate(shapePoints[index]),
                        yRatioToCoordinate(shapePoints[index + 1])
                    )

                    cubicTo(
                        xRatioToCoordinate(shapePoints[index + 2]),
                        yRatioToCoordinate(shapePoints[index + 3]),
                        xRatioToCoordinate(shapePoints[index + 4]),
                        yRatioToCoordinate(shapePoints[index + 5]),
                        xRatioToCoordinate(shapePoints[index + 6]),
                        yRatioToCoordinate(shapePoints[index + 7])
                    )
                }
            }

            close()
        }

    }

    private fun updateSignalLevelClipRect() {
        signalLevelClipRect.set(shapeBounds)
        if (theme == SignalStrength.Theme.ROUNDED) {
            signalLevelClipRect.left += signalLevelClipRect.width() * ROUNDED_SHAPE_START_OFFSET
        }
        
        signalLevelClipRect.right -=
                signalLevelClipRect.width() * (1f - signalLevel.toFloat() / MAXIMUM_SIGNAL_LEVEL)
    }

    private fun xRatioToCoordinate(xRatio: Float) =
        shapeBounds.left + xRatio * shapeBounds.width()

    private fun yRatioToCoordinate(yRatio: Float) =
        shapeBounds.top + yRatio * shapeBounds.height()

    private fun getShapePointsForTheme(theme: SignalStrength.Theme): FloatArray {
        return if (theme == SignalStrength.Theme.SHARP) {
            context.loadIntArrayAsRatioPoints(R.array.signal_strength_sharp_shape_points)
        } else {
            context.loadIntArrayAsRatioPoints(R.array.signal_strength_rounded_shape_points)
        }
    }
}