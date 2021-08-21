package com.katyrin.testappache.view.customview

import android.content.Context
import android.graphics.*
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.katyrin.testappache.R
import java.util.*
import kotlin.math.abs

class PaintView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val bitmap: Bitmap by lazy {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }
    private var bitmapCanvas: Canvas? = null
    private val paintScreen: Paint = Paint()
    private val paintLine: Paint = Paint()
    private val pathMap: MutableMap<Int, Path> = HashMap()
    private val previousPointMap: MutableMap<Int, Point> = HashMap()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bitmapCanvas = Canvas(bitmap)
        bitmap.eraseColor(ContextCompat.getColor(context, R.color.white))
    }

    fun clear() {
        pathMap.clear()
        previousPointMap.clear()
        bitmap.eraseColor(ContextCompat.getColor(context, R.color.white))
        invalidate()
    }

    var drawingColor: Int
        get() = paintLine.color
        set(color) {
            paintLine.color = color
        }

    var lineWidth: Int
        get() = paintLine.strokeWidth.toInt()
        set(width) {
            paintLine.strokeWidth = width.toFloat()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, 0f, 0f, paintScreen)
        for (key in pathMap.keys) pathMap[key]?.let { canvas.drawPath(it, paintLine) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        val actionIndex = event.actionIndex
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(
                event.getX(actionIndex), event.getY(actionIndex),
                event.getPointerId(actionIndex)
            )
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex))
        } else {
            touchMoved(event)
        }
        invalidate()
        return true
    }

    private fun touchStarted(x: Float, y: Float, lineID: Int) {
        val path: Path?
        val point: Point?
        if (pathMap.containsKey(lineID)) {
            path = pathMap[lineID]
            path?.reset()
            point = previousPointMap[lineID]
        } else {
            path = Path()
            pathMap[lineID] = path
            point = Point()
            previousPointMap[lineID] = point
        }
        path?.moveTo(x, y)
        point?.x = x.toInt()
        point?.y = y.toInt()
    }

    private fun touchMoved(event: MotionEvent) {
        for (i in 0 until event.pointerCount) {
            val pointerID = event.getPointerId(i)
            val pointerIndex = event.findPointerIndex(pointerID)
            if (pathMap.containsKey(pointerID)) {
                val newX = event.getX(pointerIndex)
                val newY = event.getY(pointerIndex)
                val path = pathMap[pointerID]
                previousPointMap[pointerID]?.let { point ->
                    val deltaX = abs(newX - point.x)
                    val deltaY = abs(newY - point.y)
                    if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                        path?.quadTo(
                            point.x.toFloat(),
                            point.y.toFloat(),
                            (newX + point.x) / 2,
                            (newY + point.y) / 2
                        )
                        point.x = newX.toInt()
                        point.y = newY.toInt()
                    }
                }
            }
        }
    }

    private fun touchEnded(lineID: Int) {
        pathMap[lineID]?.let { path ->
            bitmapCanvas?.drawPath(path, paintLine)
            path.reset()
        }
    }

    fun saveImage() {
        val name = "Мой проект" + System.currentTimeMillis() + ".jpg"
        val location = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap, name, "My Drawing"
        )
        if (location != null) {
            Toast.makeText(context, "R.string.message_saved", Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, xOffset / 2, yOffset / 2)
                show()
            }
        } else {
            Toast.makeText(context, "R.string.message_error_saving", Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, xOffset / 2, yOffset / 2)
                show()
            }
        }
    }

    companion object {
        private const val TOUCH_TOLERANCE = 10f
    }

    init {
        paintLine.isAntiAlias = true
        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f
        paintLine.strokeCap = Paint.Cap.ROUND
    }
}