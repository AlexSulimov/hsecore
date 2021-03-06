package com.hse.core.ui.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import com.hse.core.R
import com.hse.core.common.color
import com.hse.core.common.dip
import com.hse.core.common.dp

class BorderedEditText : EditText, View.OnFocusChangeListener {

    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs, android.R.attr.editTextStyle)


    var title: String? = null
        set(value) {
            field = value
            titleWidth = textPaint.measureText(value)
            if (hint == null) hint = field
            invalidate()
        }

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = color(R.color.textPrimary)
        alpha = 0
        textSize = dp(12f)
    }
    private var titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = color(R.color.windowBackground)
        alpha = 0
    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (forceShowTitle) return

            if (s?.isNotEmpty() == true && before == 0 && titleAlpha == 0) animateTitle(0f, 1f)
            else if (s?.length == 0 && titleAlpha == 255) animateTitle(1f, 0f)
        }
    }

    var forceShowTitle = false
        set(value) {
            field = value
            if (value) {
                textPaint.alpha = 255
                titlePaint.alpha = 255
                titleAlpha = 255
            }
        }

    private var titleAlpha = 0
    private var titleWidth = 0f
    private val titleTop = dp(12f)
    private val textMarginLeft = dp(16f)
    private val textMicroMargin = dp(4f)

    init {
        //TODO если gravity==top, ставить верный paddingTop
        setBackgroundResource(R.drawable.bordered_edit_text_selector)
        setPadding(dip(16f), 0, dip(16f), 0)
        setTextColor(color(R.color.textPrimary))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firstBaselineToTopHeight = dip(28f)
            lastBaselineToBottomHeight = dip(8f)
        }
        minHeight = dip(60f)
        minimumHeight = dip(60f)
        onFocusChangeListener = this
        addTextChangedListener(textWatcher)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        title?.let {
            val left = textMarginLeft - textMicroMargin
            canvas.drawRect(
                left,
                0f,
                left + titleWidth + textMicroMargin * 2,
                dp(12f),
                titlePaint
            )
            canvas.drawText(it, textMarginLeft, titleTop, textPaint)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            textPaint.color = color(R.color.blue)
            textPaint.alpha = titleAlpha
        } else {
            textPaint.color = color(R.color.textPrimary)
            textPaint.alpha = titleAlpha
        }
    }

    private fun animateTitle(from: Float, to: Float) {
        ValueAnimator.ofFloat(from, to).apply {
            duration = 100
            addUpdateListener {
                val alpha = (255f * it.animatedValue as Float).toInt()
                textPaint.alpha = alpha
                titlePaint.alpha = alpha
                titleAlpha = alpha
                invalidate()
            }
        }.start()
    }

}