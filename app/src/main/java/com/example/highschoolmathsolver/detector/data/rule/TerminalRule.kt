package com.example.highschoolmathsolver.detector.data.rule

import com.example.highschoolmathsolver.detector.data.Hypothesis
import org.opencv.core.Rect

class TerminalRule(
    val A: String,
    val s: String,
    val prob : Double,
    val template: String
) : Rule {
    override fun mergeRegions(region: Rect, b: Hypothesis, c: Hypothesis): Hypothesis {
        return b
    }
}