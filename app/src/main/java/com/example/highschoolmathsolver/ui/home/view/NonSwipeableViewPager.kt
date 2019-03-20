package com.example.highschoolmathsolver.ui.home.view

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NonSwipeableViewPager : androidx.viewpager.widget.ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }
}
