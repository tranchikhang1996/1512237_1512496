package com.example.highschoolmathsolver.detector.data

import com.example.highschoolmathsolver.extentions.center
import org.opencv.core.Rect

class Hypothesis(val region: Rect) {
    var lcen: Double = region.center().y
    var rcen: Double = region.center().y
}