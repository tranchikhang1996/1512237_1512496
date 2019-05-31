package com.example.highschoolmathsolver.detector.data.rule

import com.example.highschoolmathsolver.detector.data.Hypothesis
import org.opencv.core.Rect

interface Rule {
    fun mergeRegions(region : Rect, b : Hypothesis, c : Hypothesis) : Hypothesis
}