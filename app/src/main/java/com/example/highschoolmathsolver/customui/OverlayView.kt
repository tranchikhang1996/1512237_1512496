package com.example.highschoolmathsolver.customui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.detector.listener.FrameSizeChangeListener
import com.example.highschoolmathsolver.ui.scan.fragment.ScanFragment
import org.opencv.core.Rect
import kotlin.math.abs

class OverlayView : ImageView {

    private var rect: RectF? = null
    private var rectWidth = 200f
    private var rectHeight = 100f
    private var frameColor = Color.WHITE
    private var overlayColor = Color.BLACK
    private var indicatorColor = Color.WHITE
    private var indicatorThickness = 1f
    private var frame = 1
    private val eraser = Paint(ANTI_ALIAS_FLAG).apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private val drawPaint = Paint()
    private val framePaint = Paint()
    private val indicatorPaint : Paint by lazy { Paint().apply {
        color = indicatorColor
        strokeWidth = indicatorThickness
    } }
    private var scale_X = 1f
    private var scale_Y = 1f
    private lateinit var sgd: ScaleGestureDetector
    private var indicatorRunning : Boolean = true
    private var current = 0f
    private var shouldStopIndicate : Boolean = true
    var frameSizeChangeListener : FrameSizeChangeListener? = null


    private val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (Math.abs(detector.currentSpanX - detector.previousSpanX) > Math.abs(detector.currentSpanY - detector.previousSpanY)) {
                scale_X *= detector.scaleFactor
                scale_X = Math.max(0.5f, Math.min(scale_X, 1.2f))
            } else {
                scale_Y *= detector.scaleFactor
                scale_Y = Math.max(0.5f, Math.min(scale_Y, 2f))
            }
            indicatorRunning = false
            invalidate()
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            onchangeFrameSize(width, height)
        }
    }

    private fun onchangeFrameSize(w: Int, h: Int) {
        rect?.let {
            val left = (w - rectWidth * scale_X) / 2
            val right = left + rectWidth * scale_X
            val top = (h - rectHeight * scale_Y) / 2
            val bottom = top + rectHeight * scale_Y

            val cropL = (top / h) * ScanFragment.CAMERA_PREVIEW_DEFAULT_WIDTH
            val cropT = (left / w) * ScanFragment.CAMERA_PREVIEW_DEFAULT_HEIGHT
            val cropR = (bottom / h) * ScanFragment.CAMERA_PREVIEW_DEFAULT_WIDTH
            val cropB = (right / w) * ScanFragment.CAMERA_PREVIEW_DEFAULT_HEIGHT

            if (left < 0 || top < 0) {
                return
            }

            frameSizeChangeListener?.onFrameSizeChange(
                Rect(
                    cropL.toInt(),
                    cropT.toInt(),
                    (cropR - cropL).toInt(),
                    (cropB - cropT).toInt()
                )
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onchangeFrameSize(w, h)
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OverlayView)
        rectWidth = typedArray.getDimensionPixelSize(R.styleable.OverlayView_window_width, 0).toFloat()
        rectHeight = typedArray.getDimensionPixelSize(R.styleable.OverlayView_window_height, 0).toFloat()
        indicatorColor = typedArray.getColor(R.styleable.OverlayView_line_color, Color.WHITE)
        indicatorThickness = typedArray.getDimensionPixelSize(R.styleable.OverlayView_line_thickness, 1).toFloat()
        frame = typedArray.getInt(R.styleable.OverlayView_line_speed, 1)
        overlayColor = typedArray.getColor(R.styleable.OverlayView_color, Color.BLACK)
        frameColor = typedArray.getColor(R.styleable.OverlayView_frame_color, Color.WHITE)
        typedArray.recycle()
        initView()
    }

    private fun initView() {
        sgd = ScaleGestureDetector(context, listener)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        drawPaint.color = overlayColor
        rect = RectF(0f, 0f, 0f, 0f)
        framePaint.color = frameColor
        framePaint.strokeWidth = 8f
    }

    fun stopIndicate() {
        shouldStopIndicate = true
    }

    fun startIndicator() {
        shouldStopIndicate = false
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        sgd.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPaint.style = Paint.Style.FILL
        canvas.drawPaint(drawPaint)

        rect?.let {
            it.left = (width - rectWidth * scale_X) / 2
            it.right = it.left + rectWidth * scale_X
            it.top = (height - rectHeight * scale_Y) / 2
            it.bottom = it.top + rectHeight * scale_Y
            val radiusX = it.width() / 10
            val radiusY = it.height() / 10
            canvas.drawRect(it, eraser)
            canvas.let { paper ->
                drawCorner(paper, it.left, it.top, 1, 1, radiusX, radiusY)
                drawCorner(paper, it.right, it.top, -1, 1, radiusX, radiusY)
                drawCorner(paper, it.right, it.bottom, -1, -1, radiusX, radiusY)
                drawCorner(paper, it.left, it.bottom, 1, -1, radiusX, radiusY)
                if(indicatorRunning) {
                    drawIndicator(paper, it)
                } else {
                    current = 0f
                    frame = abs(frame)
                    indicatorRunning = true
                }
            }
            if(!shouldStopIndicate) {
                invalidate()
            }
        }
    }

    private fun drawCorner(canvas: Canvas, x: Float, y: Float, dx: Int, dy: Int, radiusX: Float, radiusY: Float) {
        canvas.drawLine(x, y + dy * 4, x + dx * radiusX, y + dy *4, framePaint)
        canvas.drawLine(x + dx * 4, y, x + dx * 4, y + dy * radiusY, framePaint)
    }

    private fun drawIndicator(canvas: Canvas, rect : RectF) {
        val y = when {
            rect.top + current + frame > rect.bottom -> rect.bottom
            rect.top + current + frame < rect.top -> rect.top
            else -> rect.top + current + frame
        }
        if (y == rect.top || y == rect.bottom) {
            frame = -frame
        }
        current = y - rect.top
        canvas.drawLine(rect.left, y, rect.right, y, indicatorPaint)
    }


}