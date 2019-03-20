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


class OverlayView : ImageView {

    private var rect: RectF? = null
    private var rectWidth = 200f
    private var rectHeight = 100f
    private var color = Color.BLACK
    private val paint = Paint(ANTI_ALIAS_FLAG)
    private val fillPaint = Paint()
    private val linePaint = Paint()
    private val mode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private var scale_X = 1f
    private var scale_Y = 1f
    private lateinit var sgd : ScaleGestureDetector

    val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if(Math.abs(detector.currentSpanX - detector.previousSpanX) > Math.abs(detector.currentSpanY - detector.previousSpanY)) {
                scale_X *= detector.scaleFactor
                scale_X = Math.max(0.5f, Math.min(scale_X, 2f))
            }
            else {
                scale_Y *= detector.scaleFactor
                scale_Y = Math.max(0.5f, Math.min(scale_Y, 2f))
            }
            invalidate()
            return true
        }
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OverlayView)
        rectWidth = typedArray.getDimensionPixelSize(R.styleable.OverlayView_window_width, 0).toFloat()
        rectHeight = typedArray.getDimensionPixelSize(R.styleable.OverlayView_window_height, 0).toFloat()
        color = typedArray.getColor(R.styleable.OverlayView_color, Color.BLACK)
        typedArray.recycle()
        initView()
    }

    fun getRectWidth() : Float = rectWidth * scale_X

    fun getRectHeight() : Float = rectHeight * scale_Y

    fun setRect(rect: RectF, width: Int, height: Int) {
        this.rect = rect
        this.rectWidth = width.toFloat()
        this.rectHeight = height.toFloat()

        postInvalidate()
    }

    private fun initView() {
        sgd = ScaleGestureDetector(context, listener)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        fillPaint.color = color
        rect = RectF(0f, 0f, 0f, 0f)
        linePaint.color = Color.WHITE
        linePaint.strokeWidth = 4f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        sgd.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        fillPaint.style = Paint.Style.FILL
        canvas.drawPaint(fillPaint)

        rect?.let {
            it.left = (width - rectWidth *scale_X) / 2
            it.right = it.left + rectWidth*scale_X
            it.top = (height - rectHeight*scale_Y) / 2
            it.bottom = it.top + rectHeight*scale_Y
            paint.xfermode = mode
            canvas.drawRect(it, paint)
            canvas.drawLine(it.left, it.top, it.right, it.top, linePaint)
            canvas.drawLine(it.right, it.top, it.right, it.bottom, linePaint)
            canvas.drawLine(it.right, it.bottom, it.left, it.bottom, linePaint)
            canvas.drawLine(it.left, it.bottom, it.left, it.top, linePaint)
        }
    }
}