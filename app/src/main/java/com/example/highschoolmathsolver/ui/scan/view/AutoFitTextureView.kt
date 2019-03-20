package com.example.highschoolmathsolver.ui.scan.view

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView
import android.view.View

class AutoFitTextureView : TextureView {
    private var mRatioWidth = 0
    private var mRatioHeight = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setAspectRatio(width: Int, height: Int) {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative.")
        }
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        when {
            0 == mRatioWidth || 0 == mRatioHeight -> setMeasuredDimension(width, height)

            width < height * mRatioWidth / mRatioHeight -> setMeasuredDimension(width, width * mRatioHeight / mRatioWidth)

            else -> setMeasuredDimension(height * mRatioWidth / mRatioHeight, height)
        }
    }
}