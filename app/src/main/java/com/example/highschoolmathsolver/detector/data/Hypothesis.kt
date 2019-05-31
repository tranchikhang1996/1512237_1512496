package com.example.highschoolmathsolver.detector.data

import com.example.highschoolmathsolver.extentions.center
import com.example.highschoolmathsolver.extentions.t
import org.opencv.core.Rect

class Hypothesis(val region: Rect) {
    var lcen: Double = region.center().y
    var rcen: Double = region.center().y
    var lsub: Double = region.t.toDouble()
    var rsub: Double = region.t.toDouble()
    var lsup: Double = region.y.toDouble()
    var rsup: Double = region.y.toDouble()
}