package com.katyrin.testappache.view.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.katyrin.testappache.R
import com.katyrin.testappache.model.entities.ArrayStroke
import com.katyrin.testappache.model.entities.ParcelPath
import com.katyrin.testappache.model.entities.Stroke
import com.katyrin.testappache.utils.getColorByThemeAttr
import java.util.*
import kotlin.math.abs


class PaintView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var mX = START_POSITION
    private var mY = START_POSITION
    private var path: ParcelPath? = null
    private val paint: Paint = Paint()
    private val strokes: ArrayList<Stroke> = arrayListOf()
    private val previousStrokes: ArrayList<Stroke> = arrayListOf()
    private var strokeWidth = DEFAULT_STROKE_WIDTH
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)
    private val bitmapCanvas: Canvas by lazy { Canvas(bitmap) }
    private var currentColor: Int = context.getColorByThemeAttr(R.attr.alwaysBlackColor)
    private var background: Bitmap? = null
    private val bitmap: Bitmap by lazy {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    fun getStrokes(): ArrayStroke = ArrayStroke(strokes, previousStrokes)

    fun setStrokes(arrayStroke: ArrayStroke) {
        strokes.addAll(arrayStroke.strokes)
        previousStrokes.addAll(arrayStroke.previousStrokes)
    }

    fun setStrokeColor(color: Int) {
        currentColor = color
    }

    fun setBrushSize(size: Float) {
        strokeWidth = size.toInt()
    }

    fun undo() {
        if (strokes.size != 0) {
            previousStrokes.add(strokes[strokes.size - ONE_POSITION])
            strokes.removeAt(strokes.size - ONE_POSITION)
            invalidate()
        }
    }

    fun redo() {
        if (previousStrokes.size != 0) {
            strokes.add(previousStrokes[previousStrokes.size - ONE_POSITION])
            previousStrokes.removeAt(previousStrokes.size - ONE_POSITION)
            invalidate()
        }
    }

    fun getBitmapImage(): Bitmap = bitmap

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        setBackground()

        for (stroke in strokes) {
            paint.strokeWidth = stroke.strokeWidth.toFloat()
            paint.color = stroke.color
            bitmapCanvas.drawPath(stroke.path, paint)
        }
        canvas.drawBitmap(bitmap, START_POSITION, START_POSITION, bitmapPaint)
        canvas.restore()
    }

    private fun setBackground() {
        if (background == null)
            bitmapCanvas.drawColor(context.getColorByThemeAttr(R.attr.alwaysWhiteColor))
        else
            bitmapCanvas.drawBitmap(background!!, START_POSITION, START_POSITION, bitmapPaint)
    }

    private fun touchStart(x: Float, y: Float) {
        path = ParcelPath()
        path?.let { parcelPath -> strokes.add(Stroke(strokeWidth, parcelPath, currentColor)) }
        path?.reset()
        path?.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        if (abs(x - mX) >= TOUCH_TOLERANCE || abs(y - mY) >= TOUCH_TOLERANCE) {
            path?.quadTo(mX, mY, (x + mX) / HALF_DIVIDER, (y + mY) / HALF_DIVIDER)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        path?.lineTo(mX, mY)
        previousStrokes.clear()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (parent != null && event.action == MotionEvent.ACTION_DOWN)
            parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart(x, y)
            MotionEvent.ACTION_MOVE -> touchMove(x, y)
            MotionEvent.ACTION_UP -> touchUp()
        }
        invalidate()
        return true
    }

    fun init(newBitmap: Bitmap?) {
        background = newBitmap
    }

    init {
        paint.color = currentColor
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.alpha = 0xff
    }

    companion object {
        private const val ONE_POSITION = 1
        private const val START_POSITION = 0f
        private const val TOUCH_TOLERANCE = 4f
        private const val DEFAULT_STROKE_WIDTH = 30
        private const val HALF_DIVIDER = 2
    }
}