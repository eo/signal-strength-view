package eo.view.signalstrength

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View


class SignalStrengthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.signalStrengthStyle
) : View(context, attrs, defStyleAttr), SignalStrength {

    private val signalStrengthDrawable: SignalStrengthDrawable

    override var theme: SignalStrength.Theme
        get() = signalStrengthDrawable.theme
        set(value) {
            if (theme != value) {
                signalStrengthDrawable.theme = value
                invalidate()
            }
        }

    override var signalLevel: Int
        get() = signalStrengthDrawable.signalLevel
        set(value) {
            if (signalLevel != value) {
                signalStrengthDrawable.signalLevel = value
                invalidate()
            }
        }

    override var color: Int
        get() = signalStrengthDrawable.color
        set(value) {
            if (color != value) {
                signalStrengthDrawable.color = value
                invalidate()
            }
        }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SignalStrengthView,
            defStyleAttr,
            R.style.Widget_SignalStrength
        )

        val themes = SignalStrength.Theme.values()
        val themeIndex = typedArray.getInt(R.styleable.SignalStrengthView_signalStrengthTheme, 0)
            .coerceIn(0, themes.lastIndex)

        signalStrengthDrawable = SignalStrengthDrawable(context, themes[themeIndex])

        signalLevel =
            typedArray.getInt(R.styleable.SignalStrengthView_signalStrengthLevel, signalLevel)
        color = typedArray.getColor(R.styleable.SignalStrengthView_signalStrengthColor, color)

        typedArray.recycle()

        signalStrengthDrawable.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        signalStrengthDrawable.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        signalStrengthDrawable.setBounds(left, top, right, bottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)

        signalStrengthDrawable.setPadding(left, top, right, bottom)
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)

        when (layoutDirection) {
            LAYOUT_DIRECTION_RTL -> signalStrengthDrawable.setPadding(end, top, start, bottom)
            else -> signalStrengthDrawable.setPadding(start, top, end, bottom)
        }
    }
}
