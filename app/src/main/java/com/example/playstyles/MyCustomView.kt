package com.example.playstyles

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View


class MyCustomView : View {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleTextColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleTextDimension: Float = 0f // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
            invalidateTextPaintAndMeasurements()
        }

    var exampleTextColor: Int
        get() = _exampleTextColor
        set(value) {
            _exampleTextColor = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    private var exampleTextDimension: Float
        get() = _exampleTextDimension
        set(value) {
            _exampleTextDimension = value
            invalidateTextPaintAndMeasurements()
        }

    private var exampleBackgroundDrawable: Drawable? = null

    // Новая фишка, появившаяся после редизайна:
    var exampleBackgroundOverlay: Int? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.MyCustomView,
            defStyle.takeIf { it > 0 } ?: R.attr.customViewStyle,
            R.style.Widget_MyView
        )

        _exampleString = a.getString(R.styleable.MyCustomView_customViewText)
        _exampleTextColor = a.getColor(R.styleable.MyCustomView_customViewTextColor, exampleTextColor)
        if (a.hasValue(R.styleable.MyCustomView_customViewTextDimension)) {
            _exampleTextDimension =
                a.getDimension(R.styleable.MyCustomView_customViewTextDimension, exampleTextDimension)
        }

        if (a.hasValue(R.styleable.MyCustomView_customViewBackgroundDrawable)) {
            exampleBackgroundDrawable =
                a.getDrawable(R.styleable.MyCustomView_customViewBackgroundDrawable)
        }

        if (a.hasValue(R.styleable.MyCustomView_customViewBackgroundOverlay)) {
            exampleBackgroundOverlay =
                a.getColor(R.styleable.MyCustomView_customViewBackgroundOverlay, Color.TRANSPARENT)
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = exampleTextDimension
            it.color = exampleTextColor
            textWidth = it.measureText(exampleString)
            textHeight = it.fontMetrics.bottom
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        exampleBackgroundDrawable?.let { background ->
            background.setBounds(0, 0, width, height)
            background.draw(canvas)
        }

        // Новая фишка, появившаяся после редизайна:
        drawBackgroundOverlay(canvas)

        exampleString?.let {
            canvas.drawText(
                it,
                paddingLeft + (width - textWidth) / 2,
                paddingTop + (height + textHeight) / 2,
                textPaint
            )
        }
    }

    // Новая фишка, появившаяся после редизайна:
    private fun drawBackgroundOverlay(canvas: Canvas) {
        exampleBackgroundOverlay?.let {
            canvas.drawRect(
                0f, height * 0.25f, width.toFloat(), height * 0.75f,
                Paint().apply { color = it })
        }
    }
}