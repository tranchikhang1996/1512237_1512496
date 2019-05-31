package com.example.highschoolmathsolver.detector.model

import org.opencv.core.Rect
import javax.inject.Inject

class SegmentationModel @Inject constructor() {
    fun getProb(bi: MutableList<Rect>): Double {
        return 1.0
    }
}