package com.katyrin.testappache.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.ContextCompat
import com.katyrin.testappache.AppEvent
import com.katyrin.testappache.EventBus
import com.katyrin.testappache.R
import kotlinx.coroutines.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Runnable
import kotlin.math.abs


@KoinApiExtension
class BrushSizeView(context: Context, attrs: AttributeSet?) : View(context, attrs), KoinComponent {

    private val eventBus by inject<EventBus>()
    private val center: Float by lazy { width / FLOAT_TWO }
    private val paintInnerCircle: Paint = Paint()
    private var mY = START_POSITION
    private var innerCircleRadius = INNER_CIRCLE_RADIUS
    private var goneFlag = false
    private val longPressedRunnable = Runnable { goneFlag = true }

    fun setColor(color: Int) {
        paintInnerCircle.color = color
        invalidate()
    }

    fun getBrushColor(): Int =
        paintInnerCircle.color

    fun getBrushSize(): Float = innerCircleRadius

    fun setBrushSize(size: Float) {
        innerCircleRadius = size
        scope.launch { observer() }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(center, center, CIRCLE_RADIUS, paintCircle())
        canvas.drawCircle(center, center, CIRCLE_RADIUS, paintStroke())
        canvas.drawCircle(center, center, innerCircleRadius, paintInnerCircle)
    }

    private fun paintCircle(): Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.circle_color)
        style = Paint.Style.FILL
        isDither = true
        isAntiAlias = true
        alpha = 0xff
    }

    private fun paintStroke(): Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.icon_color)
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isDither = true
        isAntiAlias = true
        alpha = 0xff
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart(event.y)
            MotionEvent.ACTION_MOVE -> if (goneFlag) touchMove(event.y)
            MotionEvent.ACTION_UP -> touchUp()
        }
        invalidate()
        return true
    }

    private fun touchStart(y: Float) {
        goneFlag = false
        handler.postDelayed(longPressedRunnable, ViewConfiguration.getLongPressTimeout().toLong())
        mY = y
    }

    private fun touchMove(y: Float) {
        handler.removeCallbacks(longPressedRunnable)
        val dy = y - mY
        if (abs(dy) >= TOUCH_TOLERANCE) {
            mY = y
            val sum = innerCircleRadius + dy / DIVIDER
            if (sum in MIN_BRUSH_SIZE..MAX_BRUSH_SIZE) {
                innerCircleRadius = sum
                invalidate()
            }
        }
    }

    private val scope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
    )

    private fun touchUp() {
        handler.removeCallbacks(longPressedRunnable)
        scope.launch { observer() }
    }

    private suspend fun observer() =
        eventBus.invokeEvent(AppEvent.UpdateBrushSize(innerCircleRadius * FLOAT_TWO))

    init {
        paintInnerCircle.apply {
            color = ContextCompat.getColor(context, R.color.always_black)
            style = Paint.Style.FILL
            isDither = true
            isAntiAlias = true
            alpha = 0xff
        }
    }

    companion object {
        private const val FLOAT_TWO = 2f
        private const val STROKE_WIDTH = 4f
        private const val CIRCLE_RADIUS = 39f
        private const val INNER_CIRCLE_RADIUS = 15f
        private const val MAX_BRUSH_SIZE = 35f
        private const val MIN_BRUSH_SIZE = 1.5f
        private const val START_POSITION = 0f
        private const val TOUCH_TOLERANCE = 2f
        private const val DIVIDER = -   50f
    }
}