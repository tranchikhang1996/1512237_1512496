package com.example.highschoolmathsolver.detector.listener

import org.opencv.core.Rect

interface FrameSizeChangeListener {
    fun onFrameSizeChange(rect: Rect)
}