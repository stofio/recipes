package com.recipegenerator.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.Dimension

class CircularProgressView(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val oval = RectF()
    private var isIncrement = true
    private var startAngle: Float = 0f
    private var sweepAngle: Float = 0f
    private var sweepStep = 4

    private var indicatorSpeed = 2f
    private var indicatorColor = Color.BLACK
    private var indicatorWidth = dpToPx(3)

    private val paint = Paint().apply {
        color = indicatorColor
        strokeWidth = indicatorWidth
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val centerX = width / 2
        val centerY = height / 2

        val radius = if (width > height) {
            height / 2
        } else {
            width / 2
        } - paddingBottom - (indicatorWidth / 2)

        oval.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        calculateStartAngle()
        calculateSweepAngle()

        canvas.drawArc(oval, startAngle, sweepAngle, false, paint)
        postInvalidate()
    }

    private fun calculateSweepAngle() {
        if (isIncrement) {
            sweepAngle += sweepStep * indicatorSpeed
        } else {
            sweepAngle -= sweepStep * indicatorSpeed
        }
        if (sweepAngle >= 360) {
            isIncrement = false
        } else if (sweepAngle <= 0) {
            isIncrement = true
        }
    }

    private fun calculateStartAngle() {
        startAngle += if (!isIncrement) {
            sweepStep * 2
        } else {
            sweepStep
        } * indicatorSpeed
        startAngle %= 360f
    }

    private fun dpToPx(@Dimension(unit = Dimension.DP) dp: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
    )

}